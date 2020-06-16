package me.jaackson.breach.common.init;

import me.jaackson.breach.BreachingContainment;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * <p>The class that registers and holds references to all items used by the game.</p>
 *
 * @author Ocelot
 */
@SuppressWarnings("unused")
public class BreachItems
{
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, BreachingContainment.MOD_ID);

    // Example
    // public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test", () -> new Item(new Item.Properties()));

}