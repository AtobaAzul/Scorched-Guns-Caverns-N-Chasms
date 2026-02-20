package net.atobaazul.scguns_cnc.common.entity;

import net.atobaazul.scguns_cnc.registries.ModItems;
import net.atobaazul.scguns_cnc.registries.ModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.item.GunItem;

public class EnergyBoltProjectileEntity extends ProjectileEntity {
    public EnergyBoltProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public EnergyBoltProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun);
    }

    private static double negRand() {
        return Math.random() > .5f ? -Math.random() : Math.random();
    }

    private void spawnImpactParticles(Level world, Vec3 hitPos) {
        LivingEntity player = this.getShooter();
        ServerLevel serverLevel = (ServerLevel) world;

        for (int i = 0; i < 30; i++) {
            Vec3 playerPos = player.getEyePosition();
            Vec3 velocity = hitPos.subtract(playerPos).normalize().scale(-1);
            Vec3 mirroredVelocity = new Vec3(-velocity.x, velocity.y, -velocity.z).scale(0.5);

            double offsetX = (world.random.nextDouble() - 0.5) * 0.2;
            double offsetY = (world.random.nextDouble() - 0.5) * 0.2;
            double offsetZ = (world.random.nextDouble() - 0.5) * 0.2;

            serverLevel.sendParticles(ModParticleTypes.ENERGY_BOLT_IMPACT.get(), hitPos.x + offsetX, hitPos.y + offsetY, hitPos.z + offsetZ, 1, mirroredVelocity.x + offsetX, mirroredVelocity.y + offsetY, mirroredVelocity.z + offsetZ, 1);
        }

        for (int i = 0; i < 30; i++) {
            Vec3 dir = new Vec3(negRand(), negRand(), negRand()).scale(0.15);
            double offsetX = (world.random.nextDouble() - 0.5) * 0.2;
            double offsetY = (world.random.nextDouble() - 0.5) * 0.2;
            double offsetZ = (world.random.nextDouble() - 0.5) * 0.2;

            serverLevel.sendParticles(ModParticleTypes.ENERGY_BOLT_IMPACT.get(), hitPos.x + offsetX, hitPos.y + offsetY, hitPos.z + offsetZ, 1, dir.x + offsetX, dir.y + offsetY, dir.z + offsetZ, 1);
        }
    }

    @Override
    public float getaFloat() {
        if (this.getWeapon().is(ModItems.SCATTERER.get())) {
            float initialDamage = (this.projectile.getDamage() + this.additionalDamage + this.attributeAdditionalDamage);
            initialDamage *= (float) this.attributeDamageMultiplier;
            if (this.projectile.isDamageReduceOverLife()) {
                float modifier = ((float) this.projectile.getLife() - (float) (this.tickCount - 1)) / (float) this.projectile.getLife();
                initialDamage *= modifier;
            }
            return initialDamage;
        } else return super.getaFloat();
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z) {
        super.onHitBlock(state, pos, face, x, y, z);
        spawnImpactParticles(this.level(), new Vec3(x, y, z));
    }

    @Override
    protected void onHitEntity(Entity entity, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot) {
        super.onHitEntity(entity, hitVec, startVec, endVec, headshot);
        spawnImpactParticles(this.level(), hitVec);

        ItemStack stack = this.getWeapon();
        if (stack.getItem() instanceof RechargeableEnergyGunItem gunItem && gunItem.getUsesOverheat()) {
            CompoundTag tag = stack.getOrCreateTag();
            float heatLevel = tag.getFloat("HeatLevel");
            float igniteChance = heatLevel / 50f;
            if (entity instanceof LivingEntity livingEntity && igniteChance >= Math.random()) {
                livingEntity.setSecondsOnFire(2);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && (this.tickCount > 1 && this.tickCount < this.life)) {
            Vec3 startVec = this.position();
            Vec3 dirVec = this.getDeltaMovement();
            Vec3 endVec = startVec.add(dirVec);

            for (float i = 0; i <= 10; i++) {
                double x = startVec.x + (i / 10) * (endVec.x - startVec.x);
                double y = startVec.y + (i / 10) * (endVec.y - startVec.y);
                double z = startVec.z + (i / 10) * (endVec.z - startVec.z);

                this.level().addParticle(ModParticleTypes.ENERGY_BOLT_TRAIL.get(), true, x, y, z, 0, 0, 0);
            }
        }
    }
}
