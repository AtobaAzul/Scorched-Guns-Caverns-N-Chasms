package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mrcrayfish.framework.api.network.LevelLocation;
import com.teamabnormals.caverns_and_chasms.common.entity.projectile.LargeArrow;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.atobaazul.scguns_cnc.common.ModTags;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.core.particles.ParticleOptions;
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
import org.spongepowered.asm.mixin.Unique;
import top.ribs.scguns.Config;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.ProjectileManager;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import top.ribs.scguns.common.network.ServerPlayHandler;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.event.GunEventBus;
import top.ribs.scguns.init.ModEnchantments;
import top.ribs.scguns.interfaces.IProjectileFactory;
import top.ribs.scguns.item.GunItem;
import top.ribs.scguns.network.PacketHandler;
import top.ribs.scguns.network.message.C2SMessageShoot;
import top.ribs.scguns.network.message.S2CMessageBulletTrail;
import top.ribs.scguns.util.GunEnchantmentHelper;

import java.util.Objects;

@Mixin(ServerPlayHandler.class)
public abstract class ServerPlayHandlerMixin {
    private static final int maxRate = 50;

    @WrapMethod(method = "fireProjectiles", remap = false)
    private static void scguns_cnc$fireProjectiles(Level world, ServerPlayer player, ItemStack heldItem, GunItem item, Gun modifiedGun, Operation<Void> original) {
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
        } else if (heldItem.is(ModItems.SCATTERER.get())) {
            float chargeProgress = player.getPersistentData().getFloat("ChargeProgress");
            CompoundTag tag = heldItem.getOrCreateTag();
            int currentAmmo = tag.getInt("AmmoCount");

            int count = Math.min(currentAmmo, Mth.floor(Mth.lerp(chargeProgress, 1, modifiedGun.getProjectile().getProjectileAmount())));
            Gun.Projectile projectileProps = modifiedGun.getProjectile(heldItem);
            ProjectileEntity[] spawnedProjectiles = new ProjectileEntity[count];

            for (int i = 0; i < count; i++) {
                IProjectileFactory factory = ProjectileManager.getInstance().getFactory(ForgeRegistries.ITEMS.getKey(projectileProps.getItem()));
                ProjectileEntity projectileEntity = factory.create(world, player, heldItem, item, modifiedGun);
                projectileEntity.setWeapon(heldItem);
                projectileEntity.setAdditionalDamage(Gun.getAdditionalDamage(heldItem));
                world.addFreshEntity(projectileEntity);
                spawnedProjectiles[i] = projectileEntity;
                projectileEntity.tick();
            }

            if (!projectileProps.shouldHideProjectile()) {
                scguns_cnc$sendProjectileTrail(player, spawnedProjectiles, projectileProps, false);
            }
        } else {
            original.call(world, player, heldItem, item, modifiedGun);
        }
    }

    @Unique
    private static void scguns_cnc$sendProjectileTrail(ServerPlayer player, ProjectileEntity[] projectiles, Gun.Projectile projectileProps, boolean b) {
        if (projectileProps.shouldHideTrail()) {
            return;
        }

        double spawnX = player.getX();
        double spawnY = player.getY() + 1.0;
        double spawnZ = player.getZ();
        double radius = Config.COMMON.network.projectileTrackingRange.get();
        ParticleOptions data = GunEnchantmentHelper.getParticle(player.getMainHandItem());

        S2CMessageBulletTrail messageBulletTrail = new S2CMessageBulletTrail(projectiles, projectileProps, player.getId(), data, true);

        PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(player.level(), spawnX, spawnY, spawnZ, radius), messageBulletTrail);
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

            if (gunItem.getUsesOverheat()) {
                float heatLevel = tag.getFloat("HeatLevel");
                tag.putFloat("HeatLevel", Math.min(heatLevel + 1, maxRate));
            }
        }
    }

    @WrapMethod(method = "consumeAmmo", remap = false)
    private static void scguns_cnc$consumeAmmo(ServerPlayer player, ItemStack heldItem, Operation<Void> original) {
        if (heldItem.is(ModItems.SCATTERER.get())) {
            if (heldItem.getItem() instanceof GunItem gunItem) {

                Gun modifiedGun = gunItem.getModifiedGun(heldItem);

                float chargeProgress = player.getPersistentData().getFloat("ChargeProgress");

                int count = (int) Math.floor(Mth.lerp(chargeProgress, 1, modifiedGun.getProjectile().getProjectileAmount()));

                if (!player.isCreative()) {
                    CompoundTag tag = heldItem.getOrCreateTag();
                    if (!tag.getBoolean("IgnoreAmmo")) {
                        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.RECLAIMED.get(), heldItem);
                        if (level == 0 || player.level().random.nextInt(4 - Mth.clamp(level, 1, 2)) != 0) {
                            int currentAmmo = tag.getInt("AmmoCount");
                            tag.putInt("AmmoCount", Math.max(0, currentAmmo - count));
                        }
                    }
                }

            }
        } else {
            original.call(player, heldItem);
        }
    }
}
