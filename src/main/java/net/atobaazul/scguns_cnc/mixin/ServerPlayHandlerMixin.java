package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.LargeArrow;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.atobaazul.scguns_cnc.common.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.ribs.scguns.Config;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import top.ribs.scguns.common.network.ServerPlayHandler;
import top.ribs.scguns.event.GunEventBus;
import top.ribs.scguns.init.ModEnchantments;
import top.ribs.scguns.item.GunItem;
import top.ribs.scguns.network.message.C2SMessageShoot;

import java.util.Objects;

import static net.atobaazul.scguns_cnc.registries.ModItems.MEDIUM_NECROMIUM_CASING;
import static net.atobaazul.scguns_cnc.registries.ModItems.SMALL_NECROMIUM_CASING;

@Mixin(ServerPlayHandler.class)
public abstract class ServerPlayHandlerMixin {
    private static final int maxRate = 50;

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
    }

    @WrapMethod(method = "handleCasingEjection", remap = false)
    private static void scguns_cnc$handleCasingEjection(ServerPlayer player, ItemStack heldItem, Gun modifiedGun, Level world, Operation<Void> original) {
        if (Config.COMMON.gameplay.spawnCasings.get()) {
            if (modifiedGun.getProjectile(heldItem).casingType != null && !player.getAbilities().instabuild && !modifiedGun.getProjectile(heldItem).ejectDuringReload()) {
                ItemStack casingStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile(heldItem).casingType)));
                if (casingStack.is(ModTags.INCREASED_CASING_DROP_CHANCE)) {
                    double baseChance = 0.50;
                    int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SHELL_CATCHER.get(), heldItem);
                    double finalChance = baseChance + (enchantmentLevel * 0.15);

                    if (Math.random() < finalChance) {
                        if (enchantmentLevel > 0) {
                            if (!GunEventBus.addCasingDirectly(player, casingStack)) {
                                GunEventBus.spawnCasingInWorld(world, player, casingStack);
                            }
                        } else {
                            if (!GunEventBus.addCasingToPouch(player, casingStack)) {
                                GunEventBus.spawnCasingInWorld(world, player, casingStack);
                            }
                        }
                    }

                } else {
                    original.call(player, heldItem, modifiedGun, world);
                }
            }
        }
    }

    @WrapMethod(method = "handleShoot", remap = false)
    private static void scguns_cnc$handleShoot(C2SMessageShoot message, ServerPlayer player, Operation<Void> original) {
        original.call(message, player);

        Level world = player.level();
        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (heldItem.getItem() instanceof RechargeableEnergyGunItem gunItem) {
            CompoundTag tag = heldItem.getOrCreateTag();
            tag.putInt("RechargeCounter", (int) Math.floor((gunItem.getRefillCooldown() * gunItem.getReloadRechargeTimeMult())));

            if (gunItem.getUseFireRateRampUp()) {
                int shotCount = tag.getInt("ShotCount");
                tag.putInt("ShotCount", Math.min(shotCount + 1, maxRate));
            }
        }
    }
}
