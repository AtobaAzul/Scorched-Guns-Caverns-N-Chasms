package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.ribs.scguns.entity.projectile.ProjectileEntity;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {
    @Shadow protected LivingEntity shooter;

    @WrapMethod(method="calculateChargeSpreadMultiplier", remap = false)
    private float scguns_cnc$calculateChargeSpreadMultiplier(float chargeProgress, Operation<Float> original) {
        ItemStack weapon = this.shooter.getMainHandItem();
        if (weapon.is(ModItems.SCATTERER.get())) {
            chargeProgress = Mth.clamp(chargeProgress, 0.0F, 1.0F);
            float minChargeSpreadMultiplier = 0.3F;
            float maxChargeSpreadMultiplier = 2.0F;
            return minChargeSpreadMultiplier - (minChargeSpreadMultiplier - maxChargeSpreadMultiplier) * chargeProgress * chargeProgress;
        } else {
            return original.call(chargeProgress);
        }
    }

    @WrapMethod(method = "calculateChargeSpeedMultiplier", remap = false)
    private float scguns_cnc$calculateChargeSpeedMultiplier(float chargeProgress, Operation<Float> original) {
        ItemStack weapon = this.shooter.getMainHandItem();
        if (weapon.is(ModItems.SCATTERER.get())) {
            return 1.0F;
        }
        return original.call(chargeProgress);
    }
}
