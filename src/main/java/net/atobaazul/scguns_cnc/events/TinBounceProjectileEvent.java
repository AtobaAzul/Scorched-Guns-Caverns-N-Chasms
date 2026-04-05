package net.atobaazul.scguns_cnc.events;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.common.block.TinSoundType;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.other.CCUtil;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.atobaazul.scguns_cnc.common.entity.projectile.BouncingProjectileEntity;
import net.atobaazul.scguns_cnc.common.event.BulletDeflectEvent;
import net.atobaazul.scguns_cnc.registries.ModEntities;
import net.atobaazul.scguns_cnc.util.GrazerExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.ribs.scguns.entity.projectile.LightningProjectileEntity;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.event.GunProjectileHitEvent;

import static com.teamabnormals.caverns_and_chasms.core.other.CCUtil.playRicochetEffects;
import static net.atobaazul.scguns_cnc.SCGunsCnC.LOGGER;
import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class TinBounceProjectileEvent {
    public static void deflectProjectileRaw(Entity projectile, double xMovement, double yMovement, double zMovement, double x, double y, double z) {
        IDataManager data = (IDataManager) projectile;
        data.setValue(CCDataProcessors.RICOCHETS, data.getValue(CCDataProcessors.RICOCHETS) + 1);

        data.setValue(CCDataProcessors.DEFLECT_X, xMovement);
        data.setValue(CCDataProcessors.DEFLECT_Y, yMovement);
        data.setValue(CCDataProcessors.DEFLECT_Z, zMovement);
        data.setValue(CCDataProcessors.SHOULD_DEFLECT, true);
        projectile.setDeltaMovement(Vec3.ZERO);
        projectile.setPos(x, y, z);
        //Since in our use case of ProjectileEntity the original code's `checkInsideBlocks` does not exist, we call tick instead.
        projectile.tick();
    }

    public static void deflectProjectileRaw(Entity projectile, Vec3 deflectMovement, Vec3 deflectLocation) {
        deflectProjectileRaw(projectile, deflectMovement.x, deflectMovement.y, deflectMovement.z, deflectLocation.x, deflectLocation.y, deflectLocation.z);
    }

    public static boolean deflectBulletProjectile(Level level, ProjectileEntity projectile, HitResult hitResult, Vec3 oldMovement, Vec3 deflectMovement, Vec3 deflectLocation, SoundEvent soundEvent) {
        BulletDeflectEvent.Pre deflectEventPre = BulletDeflectEvent.onProjectileDeflectPre(projectile, hitResult, deflectMovement, deflectLocation, soundEvent);
        if (!deflectEventPre.isCanceled()) {
            BulletDeflectEvent.Post deflectEventPost = BulletDeflectEvent.onProjectileDeflectPost(projectile, hitResult, deflectEventPre.getDeflectedMovement(), deflectEventPre.getDeflectLocation(), deflectEventPre.getSoundEvent());
            deflectProjectileRaw(projectile, deflectEventPost.getDeflectedMovement(), deflectEventPost.getDeflectLocation());
            playRicochetEffects(level, deflectLocation, oldMovement.reverse().normalize(), oldMovement.lengthSqr(), deflectEventPost.getSoundEvent(), soundEvent == CCSoundEvents.STORAGE_DUCT_DEFLECT.get() ? 0.5F : 1.0F, level.random, true);
            return true;
        }
        return false;
    }

    public static boolean isBulletProjectileBlocked(LivingEntity living, Entity projectile) {
        boolean piercing = projectile.getType().is(CCEntityTypeTags.NOT_DEFLECTED_BY_TIN);
        //assume no bounce = pierce?
        //dunno if this is a good assumption.

        if (living.isBlocking() && !piercing) {
            Vec3 projectilePos = projectile.position();
            Vec3 viewVector = living.getViewVector(1.0F);
            Vec3 vec = projectilePos.vectorTo(living.position()).normalize();
            vec = new Vec3(vec.x, 0.0D, vec.z);
            return vec.dot(viewVector) < 0.0D;
        }

        return false;
    }

    @SubscribeEvent
    public static void onProjectileHit(GunProjectileHitEvent event) {
        Level level = event.getProjectile().level();
        RandomSource random = level.getRandom();
        ProjectileEntity projectile = event.getProjectile();
        IDataManager data = (IDataManager) projectile;
        HitResult hitResult = event.getRayTrace();
        Vec3 movement = projectile.getDeltaMovement();

        //Crashes. Also, instanceof check because it's not actually registered as an entity.
        if (projectile instanceof LightningProjectileEntity) return;

        boolean ricoshotBullet = projectile.getType() == ModEntities.RICOSHOT_ROUND_PROJECTILE.get();

        if (hitResult.getType() == HitResult.Type.BLOCK && !projectile.getType().is(CCEntityTypeTags.NOT_DEFLECTED_BY_TIN)) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos origin = blockHitResult.getBlockPos();
            //BlockState originalState = level.getBlockState(origin);

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

            boolean bonus = data.getValue(CCDataProcessors.BONUS_DEFLECT);
            if (flag || bonus || ricoshotBullet) {
                double speed = movement.lengthSqr();
                if (/*direction != Direction.UP ||*/ speed > 0.1D) {
                    Vec3 location = hitResult.getLocation();
                    Axis axis = direction.getAxis();
                    int i = blockHitResult.getDirection().getAxisDirection().getStep();

                    double j = 0.65D;
                    double k = 0.75D;

                    if (state.is(CCBlockTags.MAINTAINS_DEFLECT_VELOCITY)) {
                        j = 0.9D;
                        k = 0.9D;
                    }

                    if (state.is(CCBlockTags.WEAKER_DEFLECT_VELOCITY) || bonus) {
                        j -= 0.25D;
                        k -= 0.25D;
                    } else if (state.is(CCBlockTags.WEAKEST_DEFLECT_VELOCITY)) {
                        j -= 0.35D;
                        k -= 0.35D;
                    }

                    Vec3 reflect;
                    Vec3 reflectLoc;

                    if (axis == Axis.X) {
                        reflect = movement.multiply(-j, k, k);
                        reflectLoc = location.add(0.01D * i, 0.0D, 0.0D);
                    } else if (axis == Axis.Y) {
                        reflect = movement.multiply(k, -j, k);
                        reflectLoc = location.add(0.0D, i == 1 ? 0.0D : -0.01D, 0.0D);
                    } else {
                        reflect = movement.multiply(k, k, -j);
                        reflectLoc = location.add(0.0D, 0.0D, 0.01D * i);
                    }

                    SoundType soundType = state.getBlock().getSoundType(state, level, pos, null);
                    SoundEvent soundEvent = ricoshotBullet ? CCSoundEvents.RICOCHET_ARROW_DEFLECT.get() : bonus ? CCSoundEvents.TINPLATE_SECOND_DEFLECT.get() : soundType instanceof TinSoundType tinSoundType ? tinSoundType.getDeflectSound() : CCSoundEvents.TIN_DEFLECT.get();

                    if (deflectBulletProjectile(level, projectile, hitResult, movement, reflect, reflectLoc, soundEvent)) {
                        //Since the original onProjectileHit expects a `Projectile` and FOR SOME REASON cgm's ProjectileEntity does not
                        //inherit Projectile, we cannot call this. VVV
                        //originalState.onProjectileHit(level, originalState, blockHitResult, projectile);

                        if (ricoshotBullet && projectile instanceof BouncingProjectileEntity bProjectile) {
                            LOGGER.debug("Add crit chance to bullet");
                            bProjectile.addBounceCritChance(.5f);
                        }

                        event.setCanceled(true);
                    }
                }
            }
        } else if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            if (entityHitResult.getEntity() instanceof LivingEntity living && living.isBlocking() && isBulletProjectileBlocked(living, projectile) && (living.getUseItem().is(CCItems.AEGIS.get()) || ricoshotBullet)) {
                AABB aabb = living.getBoundingBox().inflate(0.3D);
                Vec3 location = aabb.clip(projectile.position(), projectile.position().add(projectile.getDeltaMovement())).or(() -> aabb.clip(projectile.position(), new Vec3(living.getX(), living.getY(0.5D), living.getZ()))).orElse(projectile.position());
                Vec3 reflect = living.getLookAngle();

                if (deflectBulletProjectile(level, projectile, hitResult, movement, reflect, location, ricoshotBullet ? CCSoundEvents.RICOCHET_ARROW_DEFLECT.get() : CCSoundEvents.AEGIS_DEFLECT.get())) {
                    event.setCanceled(true);
                }
            } else if (entityHitResult.getEntity() instanceof GrazerPart grazerpart && grazerpart.deflectsAttacks() && !projectile.getType().is(CCEntityTypeTags.NOT_DEFLECTED_BY_TIN)) {
                AbstractGrazer grazer = grazerpart.getParent();
                GrazerExtension eGrazer = (GrazerExtension) grazerpart.getParent();

                if (!eGrazer.scguns_cnc$bulletProjectileJustDeflected(projectile)) {
                    AABB aabb = grazerpart.getBoundingBox().inflate(0.3D);
                    Vec3 location = aabb.clip(projectile.position(), projectile.position().add(projectile.getDeltaMovement())).or(() -> aabb.clip(projectile.position(), new Vec3(grazerpart.getX(), grazerpart.getY(0.5D), grazerpart.getZ()))).orElse(projectile.position());
                    Vec3 normal = grazer.calculateDeflectionNormal(location);
                    Vec3 reflect = movement.subtract(normal.scale(movement.dot(normal) * 2.0D)).scale(0.65D);
                    Vec3 reflectLoc = location.add(normal.scale(0.01D));

                    if (deflectBulletProjectile(level, projectile, hitResult, movement, reflect, reflectLoc, CCSoundEvents.GRAZER_DEFLECT.get())) {
                        event.setCanceled(true);
                    }
                }

                eGrazer.scguns_cnc$addDeflectedBulletProjectile(projectile);
                event.setCanceled(true);
            }

        }
    }

    @SubscribeEvent
    public static void onProjectileDeflectPost(BulletDeflectEvent.Post event) {
        ProjectileEntity projectile = event.getProjectile();
        Level level = projectile.level();
        IDataManager data = (IDataManager) projectile;
        HitResult hitResult = event.getRayTraceResult();

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);
            if (!blockState.is(CCBlockTags.DEFLECTS_PROJECTILES)) {
                data.setValue(CCDataProcessors.BONUS_DEFLECT, false);
            } else if (blockState.is(CCBlockTags.HAS_BONUS_DEFLECT)) {
                data.setValue(CCDataProcessors.BONUS_DEFLECT, true);
            }
        }
    }
}
