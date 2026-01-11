package net.atobaazul.scguns_cnc.common.entity.throwable;

import com.teamabnormals.caverns_and_chasms.common.item.silver.SilverItem;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.atobaazul.scguns_cnc.registries.ModEntities;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import top.ribs.scguns.Config;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.entity.throwable.GrenadeEntity;
import top.ribs.scguns.entity.throwable.ThrowableGrenadeEntity;
import top.ribs.scguns.entity.throwable.ThrowableItemEntity;

import java.util.List;

import static net.atobaazul.scguns_cnc.registries.ModSoundEvents.MALISON_EXPLOSION;

public class ThrowableMalisonGrenadeEntity extends ThrowableGrenadeEntity {
    private static final float EXPLOSION_RADIUS = 5.0f;
    public float rotation;
    public float prevRotation;

    public ThrowableMalisonGrenadeEntity(EntityType<? extends ThrowableGrenadeEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ThrowableMalisonGrenadeEntity(EntityType<? extends ThrowableGrenadeEntity> entityType, Level world, LivingEntity player) {
        super(entityType, world, player);
        this.setItem(new ItemStack(ModItems.MALISON_GRENADE.get()));
    }

    public ThrowableMalisonGrenadeEntity(Level world, LivingEntity player, int maxCookTime) {
        super(ModEntities.THROWABLE_MALISON_GRENADE.get(), world, player);
        this.setItem(new ItemStack(ModItems.MALISON_GRENADE.get()));
        this.setMaxLife(maxCookTime);
        this.setShouldBounce(true);

    }


    private static double negRand() {
        return Math.random() > .5f ? -Math.random() : Math.random();
    }

    private void playExplosionSound(Level world, Vec3 pos, Double radius) {
        float volume = (float) Math.min(3.5F, radius * 0.5F);
        float pitch = 0.9F + world.random.nextFloat() * 0.3F;

        world.playSound(null, pos.x, pos.y, pos.z, MALISON_EXPLOSION.get(), SoundSource.BLOCKS, volume, pitch);
    }

    private void spawnExplosionParticles(Level world, Vec3 explosionPos, float radius) {
        ServerLevel serverLevel = (ServerLevel) world;
        double sizeMultiplier = radius / 3.5;

        BlockPos blockPos = BlockPos.containing(explosionPos.x, explosionPos.y, explosionPos.z);
        BlockState blockAtExplosion = world.getBlockState(blockPos);

        double adjustedY;
        if (!blockAtExplosion.isAir()) {
            adjustedY = blockPos.getY() + 1.0;
        } else {
            adjustedY = explosionPos.y + 0.2;
        }

        double renderDistance = 128.0;
        List<ServerPlayer> nearbyPlayers = serverLevel.getEntitiesOfClass(ServerPlayer.class, new AABB(explosionPos.x - renderDistance, explosionPos.y - renderDistance, explosionPos.z - renderDistance, explosionPos.x + renderDistance, explosionPos.y + renderDistance, explosionPos.z + renderDistance));

        for (ServerPlayer player : nearbyPlayers) {
            serverLevel.sendParticles(player, ParticleTypes.EXPLOSION_EMITTER, true, explosionPos.x, adjustedY, explosionPos.z, 1, sizeMultiplier, 0.0, 0.0, 0.0);
        }

        for (int burstWave = 0; burstWave < 2; burstWave++) {
            int particlesInBurst = 6 + burstWave * 3;
            double burstRadius = radius * (0.3 + burstWave * 0.15);

            for (int i = 0; i < particlesInBurst; i++) {
                double angle = (i / (double) particlesInBurst) * 2 * Math.PI;
                double distance = burstRadius * (0.5 + world.random.nextDouble() * 0.5);

                double burstX = Math.cos(angle) * distance;
                double burstZ = Math.sin(angle) * distance;
                double burstY = (world.random.nextDouble() - 0.3) * radius * 0.1;

                double speedX = Math.cos(angle) * (0.2 + world.random.nextDouble() * 0.3);
                double speedY = 0.15 + world.random.nextDouble() * 0.25;
                double speedZ = Math.sin(angle) * (0.2 + world.random.nextDouble() * 0.3);

                for (ServerPlayer player : nearbyPlayers) {
                    serverLevel.sendParticles(player, ParticleTypes.FLAME, true, explosionPos.x + burstX, adjustedY + burstY, explosionPos.z + burstZ, 1, speedX, speedY, speedZ, 0.08);
                    serverLevel.sendParticles(player, CCParticleTypes.SILVER_SPARK.get(), true, explosionPos.x + burstX, adjustedY + burstY, explosionPos.z + burstZ, 1, speedX, speedY, speedZ, 0.08);
                }
            }
        }

        for (int scatter = 0; scatter < 15; scatter++) {
            double scatterRadius = radius * 1.0;
            double scatterAngle = world.random.nextDouble() * 2 * Math.PI;
            double scatterDistance = world.random.nextDouble() * scatterRadius;

            double scatterX = Math.cos(scatterAngle) * scatterDistance;
            double scatterZ = Math.sin(scatterAngle) * scatterDistance;
            double scatterY = (world.random.nextDouble() - 0.5) * radius * 0.25;

            double scatterSpeedX = (world.random.nextDouble() - 0.5) * 0.5;
            double scatterSpeedY = world.random.nextDouble() * 0.35;
            double scatterSpeedZ = (world.random.nextDouble() - 0.5) * 0.5;

            if (world.random.nextBoolean()) {
                serverLevel.sendParticles(CCParticleTypes.SILVER_SPARK.get(), explosionPos.x + scatterX, adjustedY + scatterY, explosionPos.z + scatterZ, 1, scatterSpeedX, scatterSpeedY, scatterSpeedZ, 0.08);
            } else {
                serverLevel.sendParticles(CCParticleTypes.SILVER_SPARK.get(), explosionPos.x + scatterX, adjustedY + scatterY, explosionPos.z + scatterZ, 1, scatterSpeedX, scatterSpeedY, scatterSpeedZ, 0.04);
            }
        }
    }

    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        this.prevRotation = this.rotation;
        double speed = this.getDeltaMovement().length();
        this.particleTick();
    }

    public void particleTick() {
        if (this.level().isClientSide) {
            this.level().addParticle(CCParticleTypes.SILVER_SPARK.get(), true, this.getX(), this.getY() + (double) 0.25F, this.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }

    private void applyExplosionDamage(Level world, Vec3 explosionPos, float radius, float baseDamage, Entity sourceEntity) {
        float damageRadius = radius * 2.0F;
        int minX = Mth.floor(explosionPos.x - damageRadius - 1.0D);
        int maxX = Mth.floor(explosionPos.x + damageRadius + 1.0D);
        int minY = Mth.floor(explosionPos.y - damageRadius - 1.0D);
        int maxY = Mth.floor(explosionPos.y + damageRadius + 1.0D);
        int minZ = Mth.floor(explosionPos.z - damageRadius - 1.0D);
        int maxZ = Mth.floor(explosionPos.z + damageRadius + 1.0D);

        List<Entity> entities = world.getEntitiesOfClass(Entity.class, new AABB(minX, minY, minZ, maxX, maxY, maxZ));

        ThrowableGrenadeEntity projectile = (ThrowableGrenadeEntity) sourceEntity;

        DamageSource damageSource = sourceEntity.damageSources().explosion(sourceEntity, projectile.getOwner());

        for (Entity entity : entities) {
            if (entity.ignoreExplosion()) {
                continue;
            }

            double distance = Math.sqrt(entity.distanceToSqr(explosionPos));
            double maxDamageDistance = radius;
            double maxDistance = radius * 2.0;

            if (distance >= maxDistance) {
                continue;
            }

            float damage;
            if (distance <= maxDamageDistance) {
                damage = baseDamage;
            } else {
                float falloffMultiplier = 1.0f - (float) ((distance - maxDamageDistance) / (maxDistance - maxDamageDistance));
                damage = baseDamage * falloffMultiplier;
            }

            if (entity instanceof LivingEntity livingEntity) {
                damage = applyBlastProtection(livingEntity, damage);
            }

            if (damage > 0) {
                entity.hurt(damageSource, damage);
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 1));
                    SilverItem.causeMagicDamageParticles(livingEntity);
                }
                entity.invulnerableTime = 0;

                double deltaX = entity.getX() - explosionPos.x;
                double deltaY = entity.getEyeY() - explosionPos.y;
                double deltaZ = entity.getZ() - explosionPos.z;
                double distanceToExplosion = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                if (distanceToExplosion != 0.0D) {
                    deltaX /= distanceToExplosion;
                    deltaY /= distanceToExplosion;
                    deltaZ /= distanceToExplosion;
                } else {
                    deltaX = 0.0;
                    deltaY = 1.0;
                    deltaZ = 0.0;
                }

                double knockbackStrength = Math.max(0, (1.0D - distance / maxDistance) * 0.4);
                entity.setDeltaMovement(entity.getDeltaMovement().add(deltaX * knockbackStrength, deltaY * knockbackStrength, deltaZ * knockbackStrength));
            }
        }
    }

    private float applyBlastProtection(LivingEntity target, float damage) {
        int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, target);
        if (protectionLevel > 0) {
            float reduction = protectionLevel * 0.08f;
            reduction = Math.min(reduction, 0.8f);
            damage *= (1.0f - reduction);
        }
        return damage;
    }


    private void doMagicExplosion(Entity entity) {

        Double radius = Config.COMMON.grenades.explosionRadius.get();

        ServerLevel serverLevel = (ServerLevel) this.level();

        playExplosionSound(serverLevel, entity.position(), radius);
        spawnExplosionParticles(serverLevel, entity.position(), EXPLOSION_RADIUS);
        applyExplosionDamage(serverLevel, entity.position(), EXPLOSION_RADIUS, 30, this);


        double y = this.getY() + this.getType().getDimensions().height * 0.5;

        double diameter = Math.max(radius, radius) * 1.75 + 1; //default range felt a bit too large, so I'll change this to be slightly less.
        int minX = Mth.floor(this.getX() - diameter);
        int maxX = Mth.floor(this.getX() + diameter);
        int minY = Mth.floor(y - diameter);
        int maxY = Mth.floor(y + diameter);
        int minZ = Mth.floor(this.getZ() - diameter);
        int maxZ = Mth.floor(this.getZ() + diameter);

        //Vec3 grenade = new Vec3(this.getX(), y, this.getZ());

        for (LivingEntity hitEntity : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(minX, minY, minZ, maxX, maxY, maxZ))) {
            if (hitEntity.ignoreExplosion()) continue;

            hitEntity.hurt(entity.damageSources().magic(), 15);
            serverLevel.sendParticles(CCParticleTypes.SILVER_SPARK.get(), hitEntity.getX(), hitEntity.getY() + 1.0, hitEntity.getZ(), 2, negRand(), 1, negRand(), 0.2);
        }
    }

    @Override
    public void onDeath() {
        //GrenadeEntity.createExplosion(this, Config.COMMON.grenades.explosionRadius.get().floatValue(), true);
        doMagicExplosion(this);
    }
}
