package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.ribs.scguns.client.render.gun.animated.AnimatedGunRenderer;
import top.ribs.scguns.common.Gun;

@Mixin(AnimatedGunRenderer.class)
public abstract class AnimatedGunRendererMixin {
    @Shadow private ItemStack currentRenderStack;

    @WrapMethod(method="calculateBoneVisibility", remap = false)
    private boolean scguns_cnc$calculateBoneVisibility(String boneName, Operation<Boolean> original) {
        int curr_ammo = Gun.getAmmoCount(this.currentRenderStack);
        boolean reloading = this.currentRenderStack.getOrCreateTag().getBoolean("scguns:IsReloading");
        boolean result = switch (boneName) {
            case "link0" -> curr_ammo < 5;
            case "link1" -> curr_ammo < 4;
            case "link2" -> curr_ammo < 3;
            case "link3" -> curr_ammo < 2;
            case "link4" -> curr_ammo < 1;
            case "link5" -> curr_ammo < 0;
            default -> original.call(boneName);
        };
        if (result) {
            System.out.println(boneName);
        }
        return result;
    }
}
