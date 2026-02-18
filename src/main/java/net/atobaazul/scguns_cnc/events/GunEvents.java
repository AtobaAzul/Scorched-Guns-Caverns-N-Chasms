package net.atobaazul.scguns_cnc.events;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.ribs.scguns.Config;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import top.ribs.scguns.event.GunFireEvent;
import top.ribs.scguns.item.GunItem;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.registries.ModItems.CACOPHONY;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class GunEvents {
    @SubscribeEvent
    public static void preShoot(GunFireEvent.Pre event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();

        if (!player.isCreative()) {
            if ((heldItem.getItem() instanceof RechargeableEnergyGunItem) && player.isUnderWater()) {
                event.setCanceled(true);
            }
        }
    }

    public static void ejectLinks(Level level, LivingEntity livingEntity, boolean mirror) {
        if (!level.isClientSide()) return;

        Player playerEntity = (Player) livingEntity;

        Vec3 lookVec = playerEntity.getLookAngle();
        Vec3 rightVec = new Vec3(-lookVec.z, 0, lookVec.x).normalize();
        Vec3 forwardVec = new Vec3(lookVec.x, 0, lookVec.z).normalize();

        double offsetX = (mirror ? -rightVec.x : rightVec.x) * 0.5 + forwardVec.x * 0.5;
        double offsetY = playerEntity.getEyeHeight() - 0.4;
        double offsetZ = (mirror ? -rightVec.z : rightVec.z) * 0.5 + forwardVec.z * 0.5;

        Vec3 particlePos = playerEntity.getPosition(1).add(offsetX, offsetY, offsetZ);
        ResourceLocation particleLocation = new ResourceLocation("scguns_cnc", "silver_links");

        ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(particleLocation);
        if (particleType instanceof SimpleParticleType simpleParticleType) {
            level.addParticle(simpleParticleType,
                    particlePos.x, particlePos.y, particlePos.z,
                    0, 0, 0);
        }
    }


    @SubscribeEvent
    public static void postShoot(GunFireEvent.Post event) {
        Player player = event.getEntity();
        Level level = event.getEntity().level();
        ItemStack heldItem = player.getMainHandItem();
        CompoundTag tag = heldItem.getOrCreateTag();

        if (heldItem.getItem() instanceof GunItem gunItem && heldItem.is(CACOPHONY.get())) {
            Gun gun = gunItem.getModifiedGun(heldItem);

            if (Config.COMMON.gameplay.spawnCasings.get()) {
                if (gun.getProjectile().ejectsCasing() && !gun.getProjectile().ejectDuringReload()) {
                    if (tag.getInt("AmmoCount") >= 1 || player.getAbilities().instabuild) {
                        ejectLinks(level, player, false);
                    }
                }
            }
        }
    }
}
