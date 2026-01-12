package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import top.ribs.scguns.util.GunEnchantmentHelper;

@Mixin(GunEnchantmentHelper.class)
public class GunEnchantmentHelperMixin {
    @WrapMethod(method = "getRate", remap = false)
    private static int scguns_cnc$getRate(ItemStack weapon, Gun modifiedGun, Operation<Integer> original) {
        int rate = original.call(weapon, modifiedGun);

        if (weapon.getItem() instanceof RechargeableEnergyGunItem gunItem && gunItem.getUseFireRateRampUp()) {
            CompoundTag tag = weapon.getOrCreateTag();
            int shotCount = tag.getInt("ShotCount");

            float extra_rate = 1 + ((float) shotCount / 50);

            return (int) Math.floor(rate * extra_rate);
        }

        return rate;
    }

}
