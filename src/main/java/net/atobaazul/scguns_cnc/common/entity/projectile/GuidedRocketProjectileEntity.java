package net.atobaazul.scguns_cnc.common.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.item.gun.GuidedRocketLauncherGunItem;
import top.ribs.scguns.entity.projectile.MicroJetEntity;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.item.GunItem;

import java.util.List;
import java.util.Optional;

public class GuidedRocketProjectileEntity extends MicroJetEntity {
    private static final int MAX_RANGE = 200;
    private Vec3 angle;
    private int age = 1;

    public GuidedRocketProjectileEntity(EntityType<? extends ProjectileEntity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public GuidedRocketProjectileEntity(EntityType<? extends ProjectileEntity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun);
    }

    @Override
    protected void onProjectileTick() {
        super.onProjectileTick();
        this.age++;
        this.angle = this.getDeltaMovement();
        if (this.level() instanceof ServerLevel level) {
            if (this.shooter.getMainHandItem().getItem() instanceof GuidedRocketLauncherGunItem gunItem && (gunItem.getEnergyStored(this.shooter.getMainHandItem()) > 0 || shooter instanceof Player player && player.getAbilities().instabuild)) {


                if (shooter instanceof Player player && !player.getAbilities().instabuild) {
                    if (!player.getAbilities().instabuild)
                        this.shooter.getMainHandItem().getCapability(ForgeCapabilities.ENERGY).map(energyStorage -> {
                            if (energyStorage.getEnergyStored() >= gunItem.getEnergyRequired()) {
                                energyStorage.extractEnergy(gunItem.getEnergyRequired(), false);
                                return true;
                            } else {
                                return false;
                            }
                        });
                }


                Vec3 start = this.shooter.getEyePosition(1.0F);
                Vec3 direction = this.shooter.getLookAngle();
                Vec3 end = start.add(direction.scale(MAX_RANGE));
                Vec3 currentPos = this.getPosition(1.0f);


                HitResult hitResult = this.shooter.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.shooter));
                Vec3 finalEnd = hitResult.getType() != HitResult.Type.MISS ? hitResult.getLocation() : end;

                AABB aabb = new AABB(start, finalEnd).inflate(0.5);
                List<Entity> entities = level.getEntities(this.shooter, aabb, e -> !e.isSpectator() && e.isPickable());
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

                float maxDeviation = Mth.clampedLerp( 0.5f, 0.0f, (float) this.age /gunItem.getModifiedGun(this.shooter.getMainHandItem()).getProjectile().getLife());

                double maxSpeed = gunItem.getModifiedGun(this.shooter.getMainHandItem()).getProjectile().getSpeed();
                Vec3 targetDirection = finalEnd.subtract(currentPos);
                double distance = targetDirection.length();

                if (distance < 0.05) {
                    this.angle = Vec3.ZERO;
                }

                Vec3 desiredVelocity = targetDirection.normalize().scale(maxSpeed);
                if (distance < maxSpeed) {
                    desiredVelocity = targetDirection;
                }
                Vec3 velocityDifference = desiredVelocity.subtract(this.angle);
                double diffLength = velocityDifference.length();
                if (diffLength > maxDeviation) {
                    velocityDifference = velocityDifference.normalize().scale(maxDeviation);
                }

                this.angle = this.angle.add(velocityDifference);

                this.setDeltaMovement(this.angle);

                //prox fuze
                if (!this.isRemoved()) {
                    AABB proxTrigger = new AABB(BlockPos.containing(this.position())).inflate(1.0);

                    if (!this.level().getEntitiesOfClass(LivingEntity.class, proxTrigger, entity -> entity != shooter && entity.isAlive()).isEmpty() && this.age > 5) {
                        this.onHitBlock(null, null, null, position().x, position().y, position().z);
                        this.remove(RemovalReason.KILLED);
                    }
                }
            }
        }
    }
}

