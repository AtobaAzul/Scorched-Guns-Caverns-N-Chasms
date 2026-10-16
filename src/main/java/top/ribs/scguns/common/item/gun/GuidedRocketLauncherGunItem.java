package top.ribs.scguns.common.item.gun;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import top.ribs.scguns.init.ModParticleTypes;
import top.ribs.scguns.item.GunItem;

import java.util.List;
import java.util.Optional;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GuidedRocketLauncherGunItem extends RechargeableEnergyGunItem {
    public GuidedRocketLauncherGunItem(Properties properties, String path, SoundEvent reloadSoundMagOut, SoundEvent reloadSoundMagIn, SoundEvent reloadSoundEnd, SoundEvent boltPullSound, SoundEvent boltReleaseSound, ForgeConfigSpec.IntValue energyRequired, ForgeConfigSpec.IntValue refillCooldown, ForgeConfigSpec.IntValue maxEnergy, ForgeConfigSpec.DoubleValue reloadRechargeTimeMult, boolean useOverheat) {
        super(properties, path, reloadSoundMagOut, reloadSoundMagIn, reloadSoundEnd, boltPullSound, boltReleaseSound, energyRequired, refillCooldown, maxEnergy, reloadRechargeTimeMult, useOverheat);
    }

    public GuidedRocketLauncherGunItem(Properties properties, String path, SoundEvent reloadSoundMagOut, SoundEvent reloadSoundMagIn, SoundEvent reloadSoundEnd, SoundEvent boltPullSound, SoundEvent boltReleaseSound, ForgeConfigSpec.IntValue energyRequired, ForgeConfigSpec.IntValue refillCooldown, ForgeConfigSpec.IntValue maxEnergy, ForgeConfigSpec.DoubleValue reloadRechargeTimeMult) {
        super(properties, path, reloadSoundMagOut, reloadSoundMagIn, reloadSoundEnd, boltPullSound, boltReleaseSound, energyRequired, refillCooldown, maxEnergy, reloadRechargeTimeMult);
    }



    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
        //no charging shenanigans.

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientEventHandler {
        private static int tickCounter = 0;
        private static final int TICK_DELAY = 2;
        private static final double OFFSET = 0.1;
        private static final int MAX_RANGE = 200;

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                tickCounter++;
                if (tickCounter >= TICK_DELAY) {
                    tickCounter = 0;

                    ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
                    if (heldItem.getItem() instanceof GuidedRocketLauncherGunItem) {
                        showLaserParticles(player);
                    }
                }
            }
        }

        private static void showLaserParticles(LocalPlayer player) {
            Level world = player.level();
            Vec3 start = player.getEyePosition(1.0F);
            Vec3 direction = player.getLookAngle();
            Vec3 end = start.add(direction.scale(MAX_RANGE));

            HitResult hitResult = player.level().clip(new ClipContext(
                    start, end,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));
            Vec3 finalEnd = hitResult.getType() != HitResult.Type.MISS ? hitResult.getLocation() : end;

            AABB aabb = new AABB(start, finalEnd).inflate(0.5);
            List<Entity> entities = world.getEntities(player, aabb, e -> !e.isSpectator() && e.isPickable());
            EntityHitResult entityHitResult = null;
            double closestDistance = Double.MAX_VALUE;

            for (Entity entity : entities) {
                AABB entityAABB = entity.getBoundingBox().inflate(0.3);
                Optional<Vec3> optionalHit = entityAABB.clip(start, finalEnd);
                if (optionalHit.isPresent()) {
                    Vec3 hitVec = optionalHit.get();
                    double distance = start.distanceTo(hitVec);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        entityHitResult = new EntityHitResult(entity, hitVec);
                    }
                }
            }

            if (entityHitResult != null) {
                finalEnd = entityHitResult.getLocation();
            }

            Vec3 adjustedPosition = finalEnd.subtract(direction.scale(OFFSET));
            world.addParticle(ModParticleTypes.LASER.get(),
                    adjustedPosition.x, adjustedPosition.y, adjustedPosition.z,
                    2.0D,
                    0.0D,
                    0.0D
            );
        }
    }
}
