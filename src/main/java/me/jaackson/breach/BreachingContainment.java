package me.jaackson.breach;

import me.jaackson.breach.common.init.*;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Mod(BreachingContainment.MOD_ID)
public class BreachingContainment
{
    public static final String MOD_ID = "breach";
    public static final ExecutorService POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), task -> new Thread(task, MOD_ID));
    public static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(task -> new Thread(task, MOD_ID));
    public static final Logger LOGGER = LogManager.getLogger();

    public static final BreachItemGroup TAB = new BreachItemGroup(MOD_ID + ".tab", false)
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Blocks.PUMPKIN);
        }
    };

    public BreachingContainment()
    {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::init);

        Runtime.getRuntime().addShutdownHook(new Thread(SCHEDULER::shutdown));

        MinecraftForge.EVENT_BUS.register(this);
        BreachItems.ITEMS.register(modBus);
        BreachBlocks.BLOCKS.register(modBus);
        BreachEntities.ENTITIES.register(modBus);
        BreachSounds.SOUNDS.register(modBus);
    }

    private void init(final FMLCommonSetupEvent event)
    {
    }

    @OnlyIn(Dist.CLIENT)
    public static void setup(FMLClientSetupEvent event)
    {
    }
}
