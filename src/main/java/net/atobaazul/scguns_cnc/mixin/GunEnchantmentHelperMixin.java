package net.atobaazul.scguns_cnc.mixin;


import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import top.ribs.scguns.util.GunEnchantmentHelper;

import static net.atobaazul.scguns_cnc.registries.ModItems.LUSTRE;

@Mixin(GunEnchantmentHelper.class)
public class GunEnchantmentHelperMixin {
    @WrapMethod(method = "applyElementalPopEffect", remap = false)
    private static void scguns_cnc$applyElementalPopEffect(ItemStack weapon, LivingEntity target, Operation<Void> original) {
        if (weapon.is(LUSTRE.get())) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.isArmor()) {
                    ItemStack item = target.getItemBySlot(slot);
                    if (item != null && item.isDamageableItem()) {
                        if (!(target instanceof Player player && player.isCreative())) {
                            item.hurtAndBreak(10, target, e -> e.broadcastBreakEvent(slot));
                        }
                    }
                }
            }
        }

        original.call(weapon, target);
    }
}
