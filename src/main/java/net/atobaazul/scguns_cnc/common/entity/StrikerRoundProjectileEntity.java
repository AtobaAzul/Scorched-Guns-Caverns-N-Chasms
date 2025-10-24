package net.atobaazul.scguns_cnc.common.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import top.ribs.scguns.Config;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.init.ModDamageTypes;
import top.ribs.scguns.item.GunItem;

import java.util.Iterator;
import java.util.List;

public class StrikerRoundProjectileEntity extends ProjectileEntity {
    public StrikerRoundProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public StrikerRoundProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun);
    }

    private void applySplashDamage(Vec3 center, float baseSplashDamage) {
        if (!this.level().isClientSide()) {
            List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(center.x - 2.0, center.y - 2.0, center.z - 2.0, center.x + 2.0, center.y + 2.0, center.z + 2.0));
            DamageSource splashSource = ModDamageTypes.Sources.projectile(this.level().registryAccess(), this, this.getShooter());
            Iterator iterator = nearbyEntities.iterator();

            while (iterator.hasNext()) {
                LivingEntity target = (LivingEntity) iterator.next();
                double distance = target.position().distanceTo(center);
                if (!(distance > 2.0)) {
                    float distanceRatio = (float) (distance / 2.0);
                    float damageMultiplier = 1.0F - distanceRatio * 0.3F;
                    float splashDamage = baseSplashDamage * damageMultiplier;
                    if (target == this.getShooter()) {
                        splashDamage *= 0.4F;
                    }

                    splashDamage = this.applyProjectileProtection(target, splashDamage);
                    if (splashDamage > 0.5F) {
                        target.hurt(splashSource, splashDamage);
                        target.setSecondsOnFire(2);
                    }
                }
            }
        }
    }

    private void spawnHitParticles(Vec3 hitVec) {
        if (this.level().isClientSide) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) this.level();
        for (int i = 0; i < 10; i++) {
            double offsetX = (this.level().random.nextDouble() - 0.5) * 0.3;
            double offsetY = (this.level().random.nextDouble() - 0.5) * 0.3;
            double offsetZ = (this.level().random.nextDouble() - 0.5) * 0.3;

            serverLevel.sendParticles(CCParticleTypes.SPARK.get(), hitVec.x + offsetX, hitVec.y + offsetY, hitVec.z + offsetZ, 20, .5, .5, .5, 0.2);

        }
    }

    /*@Override
    protected void onProjectileTick() {
        if (this.level().isClientSide && (this.tickCount > 1 && this.tickCount < this.life)) {
            Vec3 vel = this.getDeltaMovement().normalize().scale(1);
            for (int i = 0; i < 15; i++) {
                double offsetX = (this.level().random.nextDouble() - 0.5) * 0.3;
                double offsetY = (this.level().random.nextDouble() - 0.5) * 0.3;
                double offsetZ = (this.level().random.nextDouble() - 0.5) * 0.3;

                this.level().addParticle(CCParticleTypes.SPARK.get(), true, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, vel.x, vel.y, vel.z);
            }
        }
    }*/


    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z) {
        Vec3 hitVec = new Vec3(x, y, z);
        applySplashDamage(hitVec, this.getDamage() / 2);

        spawnHitParticles(hitVec);

        if (Config.COMMON.gameplay.enableFirePlacement.get()) {
            setBlockOnFire(pos, face);
        }

        super.onHitBlock(state, pos, face, x, y, z);
    }

    @Override
    protected void onHitEntity(Entity entity, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot) {
        applySplashDamage(hitVec, this.getDamage() / 2);

        spawnHitParticles(hitVec);

        super.onHitEntity(entity, hitVec, startVec, endVec, headshot);
    }

    private void setBlockOnFire(BlockPos pos, Direction face) {
        tryPlaceWallFire(pos, face);
        if (this.random.nextFloat() < 0.6f) {
            Direction[] adjacentFaces = getAdjacentFaces(face);
            for (Direction adjacentFace : adjacentFaces) {
                if (this.random.nextFloat() < 0.4f) {
                    tryPlaceWallFire(pos, adjacentFace);
                }
            }
        }

        if (face != Direction.UP && this.random.nextFloat() < 0.7f) {
            tryPlaceWallFire(pos, Direction.UP);
        }
    }

    private boolean canSustainFireOnFace(BlockState blockState, BlockPos pos, Direction face) {
        if (blockState.isFlammable(this.level(), pos, face)) {
            return true;
        }
        return blockState.isSolidRender(this.level(), pos);
    }

    private BlockState getWallFireState(Direction attachedFace) {
        BlockState fireState = Blocks.FIRE.defaultBlockState();
        return switch (attachedFace) {
            case UP -> fireState.setValue(FireBlock.UP, true);
            case NORTH -> fireState.setValue(FireBlock.NORTH, true);
            case SOUTH -> fireState.setValue(FireBlock.SOUTH, true);
            case EAST -> fireState.setValue(FireBlock.EAST, true);
            case WEST -> fireState.setValue(FireBlock.WEST, true);
            default -> fireState;
        };
    }

    private void tryPlaceWallFire(BlockPos pos, Direction face) {
        BlockPos offsetPos = pos.relative(face);

        if (this.level().isEmptyBlock(offsetPos)) {
            BlockState hitBlockState = this.level().getBlockState(pos);

            if (canSustainFireOnFace(hitBlockState, pos, face)) {
                BlockState fireState = getWallFireState(face.getOpposite());
                this.level().setBlock(offsetPos, fireState, 11);
            }
        }
    }

    private Direction[] getAdjacentFaces(Direction face) {
        return switch (face) {
            case UP, DOWN -> new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
            case NORTH, SOUTH -> new Direction[]{Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST};
            case EAST, WEST -> new Direction[]{Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH};
        };
    }

}
