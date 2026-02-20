package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import top.ribs.scguns.client.render.crosshair.DynamicCrosshair;
import top.ribs.scguns.common.Gun;

@Mixin(DynamicCrosshair.class)
public class DynamicCrosshairMixin {
    @WrapMethod(method = "calculateChargeSpreadMultiplier", remap = false)
    private float scguns_cnc$calculateChargeSpreadMultiplier(float chargeProgress, Gun modifiedGun, Operation<Float> original) {
        ItemStack gun = Minecraft.getInstance().player.getMainHandItem();

        if (gun.is(ModItems.SCATTERER.get())) {
            chargeProgress = Mth.clamp(chargeProgress, 0.0f, 1.0f);

            float weaponSpreadPenalty = 3.0f;

            float minSpreadMultiplier = 0.15f;
            float maxSpreadMultiplier = 1.0f + weaponSpreadPenalty;

            float curveValue = chargeProgress * chargeProgress * chargeProgress;
            return minSpreadMultiplier - (minSpreadMultiplier - maxSpreadMultiplier) * curveValue;
        } else {
            return original.call(chargeProgress, modifiedGun);
        }
    }
}
