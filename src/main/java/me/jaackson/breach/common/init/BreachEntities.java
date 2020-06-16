package me.jaackson.breach.common.init;

import me.jaackson.breach.BreachingContainment;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * <p>The class that registers and holds references to all entities used by the game.</p>
 *
 * @author Ocelot
 */
@SuppressWarnings("unused")
public class BreachEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, BreachingContainment.MOD_ID);

    // public static final RegistryObject<EntityType<TestEntity>> TEST_ENTITY = ENTITIES.register("test", () -> EntityType.Builder.<TestEntity>create(TestEntity::new, EntityClassification.MISC).build("test"));
}

