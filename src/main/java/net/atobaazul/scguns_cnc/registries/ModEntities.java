package net.atobaazul.scguns_cnc.registries;

import net.atobaazul.scguns_cnc.common.entity.*;
import net.atobaazul.scguns_cnc.common.entity.throwable.ThrowableMalisonGrenadeEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);


    public static final RegistryObject<EntityType<HexRoundProjectileEntity>> HEX_ROUND_PROJECTILE = registerBasic("hex_round_projectile", HexRoundProjectileEntity::new);

    public static final RegistryObject<EntityType<BluntshotProjectileEntity>> BLUNTSHOT = registerBasic("bluntshot_projectile", BluntshotProjectileEntity::new);

    public static final RegistryObject<EntityType<DummyProjectileEntity>> DUMMY_PROJECTILE = registerBasic("dummy_projectile", DummyProjectileEntity::new);



    public static final RegistryObject<EntityType<BloodShotProjectileEntity>> BLOOD_SHOT = registerBasic("blood_shot_projectile", BloodShotProjectileEntity::new);
    public static final RegistryObject<EntityType<StrikerRoundProjectileEntity>> STRIKER_ROUND_PROJECTILE = registerBasic("striker_round_projectile", StrikerRoundProjectileEntity::new);

    public static final RegistryObject<EntityType<ThrowableMalisonGrenadeEntity>> THROWABLE_MALISON_GRENADE = registerBasic("malison_grenade", ThrowableMalisonGrenadeEntity::new);

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
