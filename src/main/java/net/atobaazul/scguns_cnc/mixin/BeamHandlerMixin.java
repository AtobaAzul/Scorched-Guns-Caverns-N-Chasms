package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import top.ribs.scguns.client.handler.BeamHandler;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.item.animated.AnimatedGunItem;

import static net.atobaazul.scguns_cnc.registries.ModItems.LUSTRE;


@Mixin(BeamHandler.class)
public abstract class BeamHandlerMixin {
    private static double negRand() {
        return Math.random() > .5f ? -Math.random() : Math.random();
    }

    @WrapMethod(method="spawnBeamImpactParticles", remap = false)
    private static void scguns_cnc$spawnBeamImpactParticles(ClientLevel world, Vec3 hitPos, Player player, Operation<Void> original) {
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.getItem() instanceof AnimatedGunItem && heldItem.is(LUSTRE.get())) {

            for (int i = 0; i < 30; i++) {
                Vec3 playerPos = player.getEyePosition();
                Vec3 velocity = hitPos.subtract(playerPos).normalize().scale(-1);
                Vec3 mirroredVelocity = new Vec3(-velocity.x, velocity.y, -velocity.z).scale(0.5);

                double offsetX = (world.random.nextDouble() - 0.5) * 0.2;
                double offsetY = (world.random.nextDouble() - 0.5) * 0.2;
                double offsetZ = (world.random.nextDouble() - 0.5) * 0.2;


                world.addParticle(CCParticleTypes.SPARK.get(), false,
                        hitPos.x + offsetX, hitPos.y + offsetY, hitPos.z + offsetZ, mirroredVelocity.x+offsetX, mirroredVelocity.y+offsetY, mirroredVelocity.z+offsetZ);
            }

            for (int i = 0; i < 30; i++) {
                Vec3 dir = new Vec3(negRand(), negRand(), negRand()).scale(0.15);
                double offsetX = (world.random.nextDouble() - 0.5) * 0.2;
                double offsetY = (world.random.nextDouble() - 0.5) * 0.2;
                double offsetZ = (world.random.nextDouble() - 0.5) * 0.2;
                world.addParticle(CCParticleTypes.SPARK.get(), false,
                        hitPos.x + offsetX, hitPos.y + offsetY, hitPos.z + offsetZ, dir.x+offsetX, dir.y+offsetY, dir.z+offsetZ);
            }

            world.playSound(player, new BlockPos((int) hitPos.x, (int) hitPos.y, (int) hitPos.z), CCSoundEvents.MIME_IMPERSONATE.get(), SoundSource.PLAYERS);
        } else {
            original.call(world, hitPos, player);
        }
    }
}
