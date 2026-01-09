package net.atobaazul.scguns_cnc.common.entity.throwable;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.atobaazul.scguns_cnc.registries.ModEntities;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import top.ribs.scguns.Config;
import top.ribs.scguns.entity.throwable.ThrowableGrenadeEntity;

public class ThrowableMalisonGrenadeEntity extends ThrowableGrenadeEntity {
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

    private void doMagicExplosion(Entity entity) {
        Double radius = Config.COMMON.grenades.explosionRadius.get();

        ServerLevel serverLevel = (ServerLevel) this.level();
        for (int i = 0; i < 40; i++) {
            serverLevel.sendParticles(CCParticleTypes.SILVER_SPARK.get(), this.getX(), this.getY() + 0.1, this.getZ(), 1, negRand(), 1, negRand(), 0.2);
        }
        serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 1);


        double y = this.getY() + this.getType().getDimensions().height * 0.5;

        double diameter = Math.max(radius, radius) * 2 + 1;
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
