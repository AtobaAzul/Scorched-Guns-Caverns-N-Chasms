package net.atobaazul.scguns_cnc.common.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.item.GunItem;

public class BluntshotProjectileEntity extends ProjectileEntity {
    public BluntshotProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public BluntshotProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun);
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z) {
        super.onHitBlock(state, pos, face, x, y, z);
    }

    @Override
    protected void onHitEntity(Entity entity, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot) {
        entity.invulnerableTime = 0;

        if (entity instanceof LivingEntity livingEntity) {
            Vec3 direction = entity.position().subtract(this.shooter.position()).normalize();
            livingEntity.knockback(0.4F + (8 * 0.5F), -direction.x(), -direction.z());
        }

        super.onHitEntity(entity, hitVec, startVec, endVec, headshot);
    }

    @Override
    protected void onProjectileTick() {
        if (this.level().isClientSide && (this.tickCount > 1 && this.tickCount < this.life)) {
            this.level().addParticle(CCParticleTypes.MIME_ENERGY.get(), true, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }
}
