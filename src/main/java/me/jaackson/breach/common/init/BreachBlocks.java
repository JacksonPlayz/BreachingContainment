package me.jaackson.breach.common.init;

import me.jaackson.breach.BreachingContainment;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>The class that registers and holds references to all blocks used by the game.</p>
 *
 * @author Ocelot
 */
@SuppressWarnings("unused")
public class BreachBlocks
{
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, BreachingContainment.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, BreachingContainment.MOD_ID);

    // Example
    // public static final RegistryObject<Block> TEST_BLOCK = register("test", () -> new Block(Block.Properties.create(Material.WOOD)), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS));

    /**
     * Registers the specified block with a bound {@link BlockItem} under the specified id.
     *
     * @param name  The id of the block
     * @param block The block to register
     * @return The registry reference
     */
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block)
    {
        return register(name, block, new Item.Properties().group(BreachingContainment.TAB));
    }

    /**
     * Registers the specified block with a bound {@link BlockItem} under the specified id.
     *
     * @param name           The id of the block
     * @param block          The block to register
     * @param itemProperties The properties of the block item to register
     * @return The registry reference
     */
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, Item.Properties itemProperties)
    {
        return register(name, block, object -> new BlockItem(object.get(), itemProperties));
    }

    /**
     * Registers the specified block with a bound item under the specified id.
     *
     * @param name  The id of the block
     * @param block The block to register
     * @param item  The item to register or null for no item
     * @return The registry reference
     */
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, @Nullable Function<RegistryObject<T>, Item> item)
    {
        RegistryObject<T> object = BLOCKS.register(name, block);
        if (item != null)
            BreachItems.ITEMS.register(name, () -> item.apply(object));
        return object;
    }
}