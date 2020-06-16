package me.jaackson.breach.client;

import me.jaackson.breach.BreachingContainment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundEngineExecutor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.EXTEfx;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL11.alSource3i;
import static org.lwjgl.openal.EXTEfx.AL_EFFECT_EAXREVERB;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class ReverbHandler
{
    private static final SoundEngineExecutor SOUND_EXECUTOR;
    private static final long EFFECT_TIMEOUT = 5000;
    private static final int MAX_DURATION = 4;
    private static final Map<Integer, Integer> effectSlotLookup = new HashMap<>();

    static
    {
        SoundEngine soundEngine = ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, Minecraft.getInstance().getSoundHandler(), "field_147694_f");
        SOUND_EXECUTOR = ObfuscationReflectionHelper.getPrivateValue(SoundEngine.class, soundEngine, "field_217940_j");
    }

    private static void setReverb(int soundId, float reverb)
    {
        if (!AL.getCapabilities().ALC_EXT_EFX)
        {
            BreachingContainment.LOGGER.warn("Unable to setup reverb effects, AL EFX not supported!");
            return;
        }

        if (reverb <= 1f)
            return;

        int effectSlot = effectSlotLookup.computeIfAbsent(soundId, key -> EXTEfx.alGenAuxiliaryEffectSlots());
        EXTEfx.alAuxiliaryEffectSloti(effectSlot, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL_TRUE);

        int effect = EXTEfx.alGenEffects();
        EXTEfx.alEffecti(effect, EXTEfx.AL_EFFECT_TYPE, AL_EFFECT_EAXREVERB);
        EXTEfx.alEffectf(effect, EXTEfx.AL_EAXREVERB_DECAY_TIME, Math.min(reverb, MAX_DURATION));
        EXTEfx.alEffectf(effect, EXTEfx.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, 1f);

        EXTEfx.alAuxiliaryEffectSloti(effectSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, effect);
        EXTEfx.alDeleteEffects(effect);

        alSource3i(soundId, EXTEfx.AL_AUXILIARY_SEND_FILTER, effectSlot, 0, EXTEfx.AL_FILTER_NULL);
    }

    //TODO improve detection logic
    public static void onPlaySound(int soundId, Vec3d pos)
    {
        if (!pos.equals(Vec3d.ZERO) && Minecraft.getInstance().world != null)
        {
            final int checkRange = 32;

            PlayerEntity player = Minecraft.getInstance().player;
            World world = Minecraft.getInstance().world;
            BlockPos blockPos = new BlockPos(pos);

            BlockRayTraceResult[] results = new BlockRayTraceResult[Direction.values().length];

            for (Direction direction : Direction.values())
            {
                results[direction.getIndex()] = world.rayTraceBlocks(new RayTraceContext(pos, pos.add(new Vec3d(direction.getDirectionVec()).mul(checkRange, checkRange, checkRange)), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player));
            }

            double maxDistanceSq = 0;
            double totalDistanceSq = 0;
            for (BlockRayTraceResult result : results)
            {
                BlockPos resultPos = result.getPos();
                Direction resultHitFace = result.getFace();

                if (!world.canBlockSeeSky(resultPos))
                {
                    double distanceSq = resultPos.distanceSq(pos.x + resultHitFace.getXOffset(), pos.y + resultHitFace.getYOffset(), pos.z + resultHitFace.getZOffset(), false);
                    if (result.getType() == RayTraceResult.Type.MISS)
                        distanceSq /= 2;
                    totalDistanceSq += distanceSq;
                    if (distanceSq > maxDistanceSq)
                        maxDistanceSq = distanceSq;
                }
            }

            if (results[Direction.UP.getIndex()].getType() == RayTraceResult.Type.MISS || !world.getBlockState(results[Direction.UP.getIndex()].getPos()).causesSuffocation(world, results[Direction.UP.getIndex()].getPos()))
            {
                return;
            }

            double effect = (totalDistanceSq - maxDistanceSq) / 36;
            double length = Math.sqrt(maxDistanceSq) / 6;

            setReverb(soundId, (float) Math.pow(effect, length));
        }
    }

    public static void onDeleteSound(int soundId)
    {
        if (effectSlotLookup.containsKey(soundId))
        {
            alSource3i(soundId, EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, 0, EXTEfx.AL_FILTER_NULL);
            int effectSlot = effectSlotLookup.get(soundId);
            effectSlotLookup.remove(soundId);
            BreachingContainment.SCHEDULER.schedule(() -> SOUND_EXECUTOR.execute(() ->
            {
                EXTEfx.alAuxiliaryEffectSloti(effectSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, EXTEfx.AL_EFFECT_NULL);
                EXTEfx.alDeleteAuxiliaryEffectSlots(effectSlot);
            }), EFFECT_TIMEOUT, TimeUnit.MILLISECONDS);
        }
    }
}
