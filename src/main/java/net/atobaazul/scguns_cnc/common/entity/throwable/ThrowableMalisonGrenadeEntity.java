package net.atobaazul.scguns_cnc.common.entity.throwable;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import top.ribs.scguns.Config;
import top.ribs.scguns.entity.throwable.GrenadeEntity;
import top.ribs.scguns.entity.throwable.ThrowableItemEntity;
import top.ribs.scguns.init.ModEntities;
import top.ribs.scguns.init.ModItems;

public class ThrowableMalisonGrenadeEntity extends ThrowableItemEntity {
    public float rotation;
    public float prevRotation;

    public ThrowableMalisonGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public ThrowableMalisonGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, Level world, LivingEntity entity) {
        super(entityType, world, entity);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.05F);
        this.setItem(new ItemStack((ItemLike) ModItems.GRENADE.get()));
        this.setMaxLife(4);
    }

    public ThrowableMalisonGrenadeEntity(Level world, LivingEntity entity, int timeLeft) {
        super((EntityType) ModEntities.THROWABLE_GRENADE.get(), world, entity);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.05F);
        this.setItem(new ItemStack((ItemLike) net.atobaazul.scguns_cnc.registries.ModItems.MALISON_GRENADE.get()));
        this.setMaxLife(timeLeft);
    }

    protected void defineSynchedData() {
    }

    public void tick() {
        super.tick();
        this.prevRotation = this.rotation;
        double speed = this.getDeltaMovement().length();
        this.particleTick();
    }

    public void particleTick() {
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SMOKE, true, this.getX(), this.getY() + (double)0.25F, this.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);
        }

    }

    public void onDeath() {
        GrenadeEntity.createExplosion(this, ((Double) Config.COMMON.grenades.explosionRadius.get()).floatValue(), true);
    }
}
