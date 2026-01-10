package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.LargeArrow;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import top.ribs.scguns.common.network.ServerPlayHandler;
import top.ribs.scguns.item.GunItem;

import static net.atobaazul.scguns_cnc.registries.ModItems.LUSTRE;

@Mixin(ServerPlayHandler.class)
public abstract class ServerPlayHandlerMixin {
    @Inject(method = "fireProjectiles", at = @At("TAIL"), remap = false)
    private static void scguns_cnc$fireProjectiles(Level world, ServerPlayer player, ItemStack heldItem, GunItem item, Gun modifiedGun, CallbackInfo ci) {
        if (modifiedGun.getProjectile().getItem() == CCItems.LARGE_ARROW.get()) {

            LargeArrow arrow = new LargeArrow(world, player);

            float speed = (float) modifiedGun.getProjectile().getSpeed() * 0.35f;
            float pitch = player.getXRot();
            float yaw = player.getYRot();
            float f = -Mth.sin(yaw * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));
            float f1 = -Mth.sin(pitch * ((float) Math.PI / 180F));
            float f2 = Mth.cos(yaw * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));

            Vec3 motion = new Vec3(f, f1, f2);
            Vec3 spawnPos = player.getEyePosition().add(motion.x * 0.5, -0.1, motion.z * 0.5);
            arrow.setPos(spawnPos);
            arrow.setDeltaMovement(motion.x * speed, motion.y * speed, motion.z * speed);

            float horizontalDistance = Mth.sqrt((float) (motion.x * motion.x + motion.z * motion.z));
            arrow.setYRot((float) (Mth.atan2(motion.x, motion.z) * (180F / Math.PI)));
            arrow.setXRot((float) (Mth.atan2(motion.y, horizontalDistance) * (180F / Math.PI)));
            arrow.yRotO = arrow.getYRot();
            arrow.xRotO = arrow.getXRot();

            arrow.setBaseDamage(modifiedGun.getProjectile().getDamage() * 0.25);

            arrow.pickup = Arrow.Pickup.ALLOWED;

            world.addFreshEntity(arrow);
        }

        if (item instanceof RechargeableEnergyGunItem) {
            //reset recharge counter on shoot.
            CompoundTag tag = heldItem.getOrCreateTag();
            tag.putInt("RechargeCounter", 0);

        }
    }
}
