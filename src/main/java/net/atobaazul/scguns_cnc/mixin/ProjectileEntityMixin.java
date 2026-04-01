package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mrcrayfish.framework.api.network.LevelLocation;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.ribs.scguns.Config;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.network.PacketHandler;
import top.ribs.scguns.network.message.S2CMessageBulletTrail;
import top.ribs.scguns.util.GunEnchantmentHelper;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity implements IEntityAdditionalSpawnData {
    @Shadow
    protected LivingEntity shooter;

    @Shadow
    protected Gun modifiedGun;

    @Shadow
    protected int shooterId;
    @Shadow
    protected Gun.Projectile projectile;
    @Unique
    ProjectileEntity p = (ProjectileEntity) (Object) this;

    public ProjectileEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @WrapMethod(method = "calculateChargeSpreadMultiplier", remap = false)
    private float scguns_cnc$calculateChargeSpreadMultiplier(float chargeProgress, Operation<Float> original) {
        ItemStack weapon = this.shooter.getMainHandItem();
        if (weapon.is(ModItems.SCATTERER.get())) {
            chargeProgress = Mth.clamp(chargeProgress, 0.0F, 1.0F);
            float minChargeSpreadMultiplier = 0.3F;
            float maxChargeSpreadMultiplier = 2.0F;
            return minChargeSpreadMultiplier - (minChargeSpreadMultiplier - maxChargeSpreadMultiplier) * chargeProgress * chargeProgress;
        } else {
            return original.call(chargeProgress);
        }
    }

    @WrapMethod(method = "calculateChargeSpeedMultiplier", remap = false)
    private float scguns_cnc$calculateChargeSpeedMultiplier(float chargeProgress, Operation<Float> original) {
        ItemStack weapon = this.shooter.getMainHandItem();
        if (weapon.is(ModItems.SCATTERER.get())) {
            return 1.0F;
        }
        return original.call(chargeProgress);
    }


    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        IDataManager data = (IDataManager) this;
        if (data.getValue(CCDataProcessors.SHOULD_DEFLECT)) {
            this.setDeltaMovement(data.getValue(CCDataProcessors.DEFLECT_X), data.getValue(CCDataProcessors.DEFLECT_Y), data.getValue(CCDataProcessors.DEFLECT_Z));
            ParticleOptions _data = GunEnchantmentHelper.getParticle(this.shooter.getMainHandItem());

            ProjectileEntity[] arr = new ProjectileEntity[1];
            arr[0] = p;
            //That only handles the server side effect of the bullet, not the bullet trail, so we need to send a new message.
            S2CMessageBulletTrail messageBulletTrail = new S2CMessageBulletTrail(arr, modifiedGun.getProjectile(), shooterId, _data, true);

            PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(level(), this.getX(), this.getY(), this.getZ(), Config.COMMON.network.projectileTrackingRange.get()), messageBulletTrail);

            data.setValue(CCDataProcessors.SHOULD_DEFLECT, false);
        }
    }
}
