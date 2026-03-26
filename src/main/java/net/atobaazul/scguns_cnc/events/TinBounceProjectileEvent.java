package net.atobaazul.scguns_cnc.events;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.common.block.TinSoundType;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.AbstractGrazer;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.GrazerPart;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.atobaazul.scguns_cnc.common.entity.projectile.BouncingProjectileEntity;
import net.atobaazul.scguns_cnc.registries.ModEntities;
import net.atobaazul.scguns_cnc.util.GrazerExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.event.GunProjectileHitEvent;

import static com.teamabnormals.caverns_and_chasms.core.other.CCEvents.playRicochetEffects;
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

            boolean bonus = data.getValue(CCDataProcessors.BONUS_DEFLECT);
            boolean ricoshotRound = projectile.getType() == ModEntities.RICOSHOT_ROUND_PROJECTILE.get();


            if (flag || bonus || ricoshotRound) {
                data.setValue(CCDataProcessors.RICOCHETS, data.getValue(CCDataProcessors.RICOCHETS) + 1);
                if (!flag) {
                    data.setValue(CCDataProcessors.BONUS_DEFLECT, false);
                } else if (state.is(CCBlockTags.HAS_BONUS_DEFLECT)) {
                    data.setValue(CCDataProcessors.BONUS_DEFLECT, true);
                }

                double speed = movement.lengthSqr();
                if (direction != Direction.UP || speed > 0.04D) {
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
                    }

                    if (axis == Axis.X) {
                        data.setValue(CCDataProcessors.DEFLECT_X, -movement.x * j);
                        data.setValue(CCDataProcessors.DEFLECT_Y, movement.y * k);
                        data.setValue(CCDataProcessors.DEFLECT_Z, movement.z * k);
                        projectile.setPos(location.x + 0.01D * i, location.y, location.z);
                    } else if (axis == Axis.Y) {
                        data.setValue(CCDataProcessors.DEFLECT_X, movement.x * k);
                        data.setValue(CCDataProcessors.DEFLECT_Y, -movement.y * j);
                        data.setValue(CCDataProcessors.DEFLECT_Z, movement.z * k);
                        projectile.setPos(location.x, i == 1 ? location.y : location.y - 0.01D, location.z);
                    } else if (axis == Axis.Z) {
                        data.setValue(CCDataProcessors.DEFLECT_X, movement.x * k);
                        data.setValue(CCDataProcessors.DEFLECT_Y, movement.y * k);
                        data.setValue(CCDataProcessors.DEFLECT_Z, -movement.z * j);
                        projectile.setPos(location.x, location.y, location.z + 0.01D * i);
                    }
                    data.setValue(CCDataProcessors.SHOULD_DEFLECT, true);
                    projectile.setDeltaMovement(Vec3.ZERO);

                    if (ricoshotRound && projectile instanceof BouncingProjectileEntity bProjectile) {
                        bProjectile.addBounceCritChance(.5f);
                    }

                    projectile.tick(); //Since Gun Projectiles don't have the original method used here (checkInsideBlocks) we'll just tick the projectile.

                    SoundType soundType = state.getBlock().getSoundType(state, level, pos, null);
                    SoundEvent soundEvent = ricoshotRound ? CCSoundEvents.RICOCHET_ARROW_DEFLECT.get() : bonus ? CCSoundEvents.TINPLATE_SECOND_DEFLECT.get() : soundType instanceof TinSoundType tinSoundType ? tinSoundType.getDeflectSound() : CCSoundEvents.TIN_DEFLECT.get();
                    playRicochetEffects(level, location, movement.reverse().normalize(), speed, soundEvent, soundType == CCSoundEvents.CCSoundTypes.STORAGE_DUCT ? 0.5F : 1.0F, random, true);


                    event.setCanceled(true);


                }
            }
        } else if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            if (entityHitResult.getEntity() instanceof GrazerPart grazerpart && grazerpart.deflectsAttacks() && !projectile.getType().is(CCEntityTypeTags.NOT_DEFLECTED_BY_TIN)) {
                AbstractGrazer grazer = grazerpart.getParent();
                GrazerExtension eGrazer = (GrazerExtension) grazerpart.getParent();

                if (!eGrazer.scguns_cnc$bulletProjectileJustDeflected(projectile)) {
                    data.setValue(CCDataProcessors.RICOCHETS, data.getValue(CCDataProcessors.RICOCHETS) + 1);

                    AABB aabb = grazerpart.getBoundingBox().inflate(0.3D);
                    Vec3 location = aabb.clip(projectile.position(), projectile.position().add(projectile.getDeltaMovement())).or(() -> aabb.clip(projectile.position(), new Vec3(grazerpart.getX(), grazerpart.getY(0.5D), grazerpart.getZ()))).orElse(projectile.position());
                    Vec3 normal = grazer.calculateDeflectionNormal(location);
                    Vec3 reflect = movement.subtract(normal.scale(movement.dot(normal) * 2.0D));

                    data.setValue(CCDataProcessors.DEFLECT_X, reflect.x * 0.65D);
                    data.setValue(CCDataProcessors.DEFLECT_Y, reflect.y * 0.65D);
                    data.setValue(CCDataProcessors.DEFLECT_Z, reflect.z * 0.65D);
                    projectile.setPos(location.x + normal.x * 0.01D, location.y + normal.y * 0.01D, location.z + normal.z * 0.01D);

                    data.setValue(CCDataProcessors.SHOULD_DEFLECT, true);
                    projectile.setDeltaMovement(Vec3.ZERO);

                    playRicochetEffects(level, location, movement.reverse().normalize(), movement.lengthSqr(), CCSoundEvents.GRAZER_DEFLECT.get(), 1.0F, random, true);
                }

                eGrazer.scguns_cnc$addDeflectedBulletProjectile(projectile);
                event.setCanceled(true);
            }
        }
    }
}
