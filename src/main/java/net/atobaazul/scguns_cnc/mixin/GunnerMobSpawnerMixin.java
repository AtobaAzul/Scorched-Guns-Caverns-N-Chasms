package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.atobaazul.scguns_cnc.common.entity.AbstractGravekeeperGunnerEntity;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import top.ribs.scguns.config.GunnerMobSpawner;

@Mixin(GunnerMobSpawner.class)
public class GunnerMobSpawnerMixin {
    @WrapMethod(method = "reassessWeaponGoal", remap = false)
    private static void scguns_cnc$reassessWeaponGoal(PathfinderMob mob, Operation<Boolean> original) {
        if (!(mob instanceof AbstractGravekeeperGunnerEntity)) {
            original.call(mob);
        }
    }

    @WrapMethod(method = "hasGunAttackGoal", remap = false)
    private static boolean scguns_cnc$hasGunAttackGoal(PathfinderMob mob, Operation<Boolean> original) {
        if (mob instanceof AbstractGravekeeperGunnerEntity) {
            return true;
        }

        return original.call(mob);
    }
}
