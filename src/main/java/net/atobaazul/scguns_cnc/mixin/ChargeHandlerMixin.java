package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.ribs.scguns.common.ChargeHandler;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.item.GunItem;

import java.util.Map;
import java.util.UUID;

import static net.atobaazul.scguns_cnc.registries.ModItems.SCATTERER;
import static net.atobaazul.scguns_cnc.registries.ModSoundEvents.SCATTERER_CHARGE;

@Mixin(ChargeHandler.class)
public class ChargeHandlerMixin {
    @Shadow
    @Final
    private static Map<UUID, Integer> playerMaxChargeTime;

    @Shadow
    @Final
    private static Map<UUID, Integer> playerChargeTime;
    private static boolean fullyCharged;

    @WrapMethod(method = "updateChargeTime", remap = false)
    private static void scguns_cnc$updateChargeTime(Player player, ItemStack weapon, boolean isCharging, Operation<Void> original) {
        original.call(player, weapon, isCharging);

        if (!(weapon.getItem() instanceof GunItem gunItem && weapon.is(SCATTERER.get()))) {
            return;
        }

        Gun modifiedGun = gunItem.getModifiedGun(weapon);
        UUID playerId = player.getUUID();

        float chargeProgress = (float) playerChargeTime.getOrDefault(playerId, 0) / playerMaxChargeTime.getOrDefault(playerId, 1);

        if (playerChargeTime.getOrDefault(playerId, 0) % modifiedGun.getProjectile().getProjectileAmount() == 0 && !fullyCharged) {
            player.playSound(SCATTERER_CHARGE.get(), chargeProgress, chargeProgress);
        }

        fullyCharged = chargeProgress == 1;
    }
}
