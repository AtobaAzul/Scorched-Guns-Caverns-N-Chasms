package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.atobaazul.scguns_cnc.SCGunsCnC;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import top.ribs.scguns.common.ChargeHandler;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import top.ribs.scguns.util.GunEnchantmentHelper;

@Mixin(GunEnchantmentHelper.class)
public class GunEnchantmentHelperMixin {
    @WrapMethod(method = "getRate", remap = false)
    private static int scguns_cnc$getRate(ItemStack weapon, Gun modifiedGun, Operation<Integer> original) {
        int rate = original.call(weapon, modifiedGun);

        if (weapon.getItem() instanceof RechargeableEnergyGunItem gunItem && gunItem.getUsesOverheat()) {
            CompoundTag tag = weapon.getOrCreateTag();
            float heatLevel = tag.getFloat("HeatLevel");

            float extra_rate = 1 + (heatLevel / 15);

            return (int) Math.floor(rate * extra_rate);
        }

        return rate;
    }

    @WrapMethod(method = "getChargeDamage", remap = false)
    private static float scguns_cnc$getChargeDamage(ItemStack weapon, float damage, float chargeProgress, Operation<Float> original) {
        if (weapon.is(ModItems.SCATTERER.get())) {
            float maxDamageMult = 1.0f;
            float minDamageMult = 0.8f;

            return Mth.lerp(chargeProgress, damage * maxDamageMult, damage * minDamageMult);
        }
        return original.call(weapon, damage, chargeProgress);
    }

    @WrapMethod(method = "getRecoilModifier(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)F", remap = false)
    private static float scguns_cnc$getRecoilModifier(Player player, ItemStack weapon, Operation<Float> original) {
        float recoil = original.call(player, weapon);

        if (weapon.is(ModItems.SCATTERER.get())) {
            float chargeProgress = ChargeHandler.getChargeProgress(player, weapon);
            float extra_recoil = Mth.lerp(chargeProgress, 0, 5);

            return recoil + extra_recoil;
        }
        return recoil;
    }
}
