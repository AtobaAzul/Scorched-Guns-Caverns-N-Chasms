package net.atobaazul.scguns_cnc.mixin;


import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.ribs.scguns.client.handler.MeleeAttackHandler;
import top.ribs.scguns.item.GunItem;

import java.util.Comparator;

@Mixin(MeleeAttackHandler.class)
public class MeleeAttackHandlerMixin {
    @Unique
    private static LivingEntity scguns_cnc$raycastForMeleeAttack(Player player, ItemStack heldItem) {
        GunItem gunItem = (GunItem) heldItem.getItem();
        float reach = gunItem.getModifiedGun(heldItem).getGeneral().getMeleeReach();

        Vec3 startVec = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getLookAngle();
        Vec3 endVec = startVec.add(lookVec.scale(reach));
        AABB boundingBox = new AABB(startVec, endVec);

        return player.level().getEntitiesOfClass(LivingEntity.class, boundingBox, entity -> entity != player && entity.isAlive()).stream().min(Comparator.comparingDouble(player::distanceToSqr)).orElse(null);
    }

    @Inject(method = "performMeleeAttack", at = @At("HEAD"), remap = false)
    private static void scguns_cnc$performMeleeAttack(ServerPlayer player, CallbackInfo ci) {
        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(heldItem.getItem() instanceof GunItem gunItem)) {
            return;
        }

        LivingEntity raycastTarget = scguns_cnc$raycastForMeleeAttack(player, heldItem);
        if (heldItem.is(ModItems.ANATHEMA.get())) {
            if (raycastTarget != null) {
                if (!player.isCreative()) {
                    CompoundTag tag = heldItem.getOrCreateTag();
                    int currentAmmo = tag.getInt("AmmoCount");
                    int newAmmo = Math.min(Math.max(0, currentAmmo + 3), 30);
                    tag.putInt("AmmoCount", newAmmo);
                }
            }
        }
    }
}
