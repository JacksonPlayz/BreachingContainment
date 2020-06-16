package me.jaackson.breach.common.init;

import me.jaackson.breach.BreachingContainment;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Ocelot
 */
public class BreachSounds
{
    public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, BreachingContainment.MOD_ID);

    // public static final RegistryObject<SoundEvent> TEST_SOUND = register("test.sound");

    /**
     * Registers a new sound with the specified name.
     * @param name The name of the sound to register
     */
    private static RegistryObject<SoundEvent> register(String name)
    {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(BreachingContainment.MOD_ID, name)));
    }
}