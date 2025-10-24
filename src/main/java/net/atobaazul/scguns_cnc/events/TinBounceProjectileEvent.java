package net.atobaazul.scguns_cnc.events;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.other.CCEvents;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.ribs.scguns.entity.projectile.LightningProjectileEntity;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.event.GunProjectileHitEvent;

import static com.teamabnormals.caverns_and_chasms.core.other.CCEvents.playTinDeflectEffects;
import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class TinBounceProjectileEvent {
    @SubscribeEvent
    public static void onProjectileHit(GunProjectileHitEvent event) {
        Level level = event.getProjectile().level();
        RandomSource random = level.getRandom();
        ProjectileEntity projectile = event.getProjectile();
        IDataManager data = (IDataManager) projectile;
        HitResult hitResult = event.getRayTrace();
        Vec3 movement = projectile.getDeltaMovement();

        if (!(projectile instanceof LightningProjectileEntity)) {
            if (hitResult.getType() == HitResult.Type.BLOCK && !projectile.getType().is(CCEntityTypeTags.NOT_DEFLECTED_BY_TIN)) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockPos pos = blockHitResult.getBlockPos();
                BlockState state = level.getBlockState(pos);
                Direction direction = blockHitResult.getDirection();

                boolean flag = state.is(CCBlockTags.DEFLECTS_PROJECTILES);

                if (!flag) {
                    BlockPos blockpos1 = pos.relative(direction.getOpposite());
                    BlockState blockstate1 = level.getBlockState(blockpos1);
                    if (blockstate1.is(CCBlockTags.DEFLECTS_PROJECTILES) && blockstate1.isFaceSturdy(level, blockpos1, direction)) {
                        flag = true;
                        pos = blockpos1;
                        state = blockstate1;
                    }
                }

                if (!flag) {
                    BlockPos blockpos1 = pos.relative(direction);
                    BlockState blockstate1 = level.getBlockState(blockpos1);
                    if (blockstate1.is(CCBlockTags.DEFLECTS_PROJECTILES) && blockstate1.getCollisionShape(level, blockpos1, CollisionContext.of(projectile)).isEmpty() && blockstate1.getShape(level, blockpos1).bounds().inflate(1.0E-7D).move(blockpos1).contains(blockHitResult.getLocation())) {
                        flag = true;
                        pos = blockpos1;
                        state = blockstate1;
                    }
                }

                if (flag) {
                    double speed = movement.lengthSqr();
                    if (direction != Direction.UP || speed > 0.04D) {
                        Vec3 location = hitResult.getLocation();
                        Direction.Axis axis = direction.getAxis();
                        int i = blockHitResult.getDirection().getAxisDirection().getStep();

                        double j = 0.65D;
                        double k = 0.75D;

                        if (state.is(CCBlockTags.MAINTAINS_DEFLECT_VELOCITY)) {
                            j = 0.9D;
                            k = 0.9D;
                        }

                        if (state.is(CCBlockTags.WEAKER_DEFLECT_VELOCITY)) {
                            j -= 0.25D;
                            k -= 0.25D;
                        }

                        if (state.getBlock() instanceof TargetBlock targetBlock) {
                            //targetBlock.onProjectileHit(level, state, blockHitResult.withPosition(pos), projectile);
                        }

                        if (axis == Direction.Axis.X) {
                            data.setValue(CCDataProcessors.DEFLECT_X, -movement.x * j);
                            data.setValue(CCDataProcessors.DEFLECT_Y, movement.y * k);
                            data.setValue(CCDataProcessors.DEFLECT_Z, movement.z * k);
                            projectile.setPos(location.x + 0.01D * i, location.y, location.z);
                            projectile.setDeltaMovement(new Vec3(-movement.x * j, movement.y * k, movement.z * k));

                        } else if (axis == Direction.Axis.Y) {
                            data.setValue(CCDataProcessors.DEFLECT_X, movement.x * k);
                            data.setValue(CCDataProcessors.DEFLECT_Y, -movement.y * j);
                            data.setValue(CCDataProcessors.DEFLECT_Z, movement.z * k);
                            projectile.setPos(location.x, i == 1 ? location.y : location.y - 0.01D, location.z);
                            projectile.setDeltaMovement(new Vec3(movement.x * k, -movement.y * j, movement.z * k));

                        } else if (axis == Direction.Axis.Z) {
                            data.setValue(CCDataProcessors.DEFLECT_X, movement.x * k);
                            data.setValue(CCDataProcessors.DEFLECT_Y, movement.y * k);
                            data.setValue(CCDataProcessors.DEFLECT_Z, -movement.z * j);
                            projectile.setPos(location.x, location.y, location.z + 0.01D * i);
                            projectile.setDeltaMovement(new Vec3(movement.x * k, movement.y * k, -movement.z * j));

                        }
                        projectile.tick();
                        data.setValue(CCDataProcessors.SHOULD_DEFLECT, true);

                        SoundType soundtype = state.getBlock().getSoundType(state, level, pos, null);
                        SoundEvent soundevent = soundtype == CCSoundEvents.CCSoundTypes.STORAGE_DUCT ? CCSoundEvents.STORAGE_DUCT_DEFLECT.get() : soundtype == CCSoundEvents.CCSoundTypes.TIN_ORE ? CCSoundEvents.TIN_ORE_DEFLECT.get() : soundtype == CCSoundEvents.CCSoundTypes.DEEPSLATE_TIN_ORE ? CCSoundEvents.DEEPSLATE_TIN_ORE_DEFLECT.get() : CCSoundEvents.TIN_DEFLECT.get();
                        float pitchmultiplier = soundtype == CCSoundEvents.CCSoundTypes.STORAGE_DUCT ? 0.5F : 1.0F;


                        playTinDeflectEffects(level, location, movement.reverse().normalize(), speed, soundevent, pitchmultiplier, random);

                        if (!level.isClientSide) {
                            ServerLevel serverLevel = (ServerLevel) level;

                            for (int l = 0; l < 3; ++l) {


                                //WHYYYYYYY
                                serverLevel.sendParticles(CCParticleTypes.SPARK.get(), location.x, location.y, location.z, 5, .15, .15, .15, 0.2);
                            }
                        }
                        event.setCanceled(true);
                    }
                }
            } else if (hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                if (entityHitResult.getEntity() instanceof GrazerPart grazerpart && grazerpart.deflectsAttacks() && !projectile.getType().is(CCEntityTypeTags.NOT_DEFLECTED_BY_TIN)) {
                    AbstractGrazer grazer = grazerpart.getParent();

                    //if (!grazer.projectileJustDeflected(projectile)) {
                    AABB aabb = grazerpart.getBoundingBox().inflate(0.3D);
                    Vec3 location = aabb.clip(projectile.position(), projectile.position().add(projectile.getDeltaMovement())).or(() -> aabb.clip(projectile.position(), new Vec3(grazerpart.getX(), grazerpart.getY(0.5D), grazerpart.getZ()))).orElse(projectile.position());
                    Vec3 normal = grazer.calculateDeflectionNormal(location);
                    Vec3 reflect = movement.subtract(normal.scale(movement.dot(normal) * 2.0D));

                    data.setValue(CCDataProcessors.DEFLECT_X, reflect.x * 0.65D);
                    data.setValue(CCDataProcessors.DEFLECT_Y, reflect.y * 0.65D);
                    data.setValue(CCDataProcessors.DEFLECT_Z, reflect.z * 0.65D);
                    projectile.setPos(location.x + normal.x * 0.01D, location.y + normal.y * 0.01D, location.z + normal.z * 0.01D);

                    data.setValue(CCDataProcessors.SHOULD_DEFLECT, true);
                    projectile.setDeltaMovement(reflect);
                    projectile.tick();

                    CCEvents.playTinDeflectEffects(level, location, reflect.reverse().normalize(), movement.lengthSqr(), random);
                    if (!level.isClientSide) {
                        ServerLevel serverLevel = (ServerLevel) level;

                        for (int l = 0; l < 3; ++l) {
                            serverLevel.sendParticles(CCParticleTypes.SPARK.get(), location.x, location.y, location.z, 5, .15, .15, .15, 0.2);
                        }
                    }
                    //}

                    //grazer.addDeflectedProjectile(projectile);
                    event.setCanceled(true);
                }
            }
        }
    }


}
