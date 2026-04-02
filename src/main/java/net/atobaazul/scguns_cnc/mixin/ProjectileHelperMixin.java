package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.atobaazul.scguns_cnc.common.entity.projectile.BouncingProjectileEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "top.ribs.scguns.entity.projectile.ProjectileEntity$ProjectileHelper")
public class ProjectileHelperMixin {
    @WrapMethod(method = "handleShieldHit(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;FF)Z", remap = false)
    private static boolean scguns_cnc$handleShieldHit(Entity target, Entity projectile, float damage, float shieldDisableChance, Operation<Boolean> original) {
        if (!(target instanceof Player player)) {
            return false;
        }

        boolean isBlocking = player.isBlocking();
        boolean usingAegis = isBlocking && player.getUseItem().is(CCItems.AEGIS.get());

        if ((usingAegis || isBlocking && projectile instanceof BouncingProjectileEntity) && !projectile.getType().is(CCEntityTypeTags.NOT_DEFLECTED_BY_TIN)) {
            return true;
        }

        return original.call(target, projectile, damage, shieldDisableChance);
    }
}
