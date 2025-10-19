package net.atobaazul.scguns_cnc.registries;

import net.atobaazul.scguns_cnc.common.entity.HexRoundProjectileEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class Entities {
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);


    public static final RegistryObject<EntityType<HexRoundProjectileEntity>> HEX_ROUND_PROJECTILE = registerBasic("hex_round_projectile", HexRoundProjectileEntity::new);

    public static final RegistryObject<EntityType<HexRoundProjectileEntity>> BLUNTSHOT = registerBasic("bluntshot", HexRoundProjectileEntity::new);

    public static final RegistryObject<EntityType<HexRoundProjectileEntity>> THE_HUNGER = registerBasic("hunger", HexRoundProjectileEntity::new);


    private static <T extends Entity> RegistryObject<EntityType<T>> registerBasic(String id, BiFunction<EntityType<T>, Level, T> function)
    {
        return REGISTER.register(id, () -> EntityType.Builder.of(function::apply, MobCategory.MISC)
                .sized(0.25F, 0.25F)
                .setTrackingRange(100)
                .setUpdateInterval(1)
                .noSummon()
                .fireImmune()
                .noSave()
                .setShouldReceiveVelocityUpdates(true).build(id));
    }

}
