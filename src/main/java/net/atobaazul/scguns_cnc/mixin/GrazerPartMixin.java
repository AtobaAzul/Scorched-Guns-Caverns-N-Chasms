package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import com.teamabnormals.caverns_and_chasms.core.other.CCEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.spongepowered.asm.mixin.Mixin;
import top.ribs.scguns.entity.projectile.KrahgRoundProjectileEntity;
import top.ribs.scguns.entity.projectile.LightningProjectileEntity;
import top.ribs.scguns.entity.projectile.OsborneSlugProjectileEntity;

@Mixin(GrazerPart.class)
public abstract class GrazerPartMixin extends PartEntity<AbstractGrazer> {
    public GrazerPartMixin(AbstractGrazer parent) {
        super(parent);
    }

    @WrapMethod(method="hurt", remap = false)
    private boolean scguns_cnc$hurt(DamageSource source, float amount, Operation<Boolean> original) {
        AbstractGrazer grazer = this.getParent();
        Entity directentity = source.getDirectEntity();
        if (directentity instanceof KrahgRoundProjectileEntity || directentity instanceof OsborneSlugProjectileEntity || directentity instanceof LightningProjectileEntity) {
            AABB aabb = this.getBoundingBox().inflate(0.3D);
            Vec3 attackerpos = directentity.getEyePosition();
            Vec3 partpos = new Vec3(this.getX(), this.getY(0.5D), this.getZ());

            Vec3 location = aabb.clip(attackerpos, attackerpos.add(directentity.getViewVector(1.0F).scale(partpos.subtract(attackerpos).length() + this.getDimensions(Pose.STANDING).height * 0.5D + 0.3D))).or(() -> aabb.clip(attackerpos, partpos)).orElse(partpos);
            Vec3 normal = grazer.calculateDeflectionNormal(location);

            CCEvents.playTinDeflectEffects(this.level(), location, normal, 0.8F, this.random);

            return grazer.hurt(source, amount);
        }
        return original.call(source, amount);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
}
