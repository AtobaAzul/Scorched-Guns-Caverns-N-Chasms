package net.atobaazul.scguns_cnc.common.entity.projectile;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.atobaazul.scguns_cnc.SCGunsCnC;
import net.atobaazul.scguns_cnc.registries.ModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
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
import top.ribs.scguns.util.GunModifierHelper;

public class BouncingProjectileEntity extends ProjectileEntity {
    public float bounceCritChance;

    public BouncingProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn) {
        super(entityType, worldIn);
        this.bounceCritChance = 0.0f;
    }

    public BouncingProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun);
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z) {
        super.onHitBlock(state, pos, face, x, y, z);
    }

    public void addBounceCritChance(float num) {
        this.bounceCritChance = this.bounceCritChance + num;
    }

    public float getBounceCritChance() {
        return this.bounceCritChance;
    }

    @Override
    public float getCriticalDamage(ItemStack weapon, RandomSource rand, float damage) {
        float chance = GunModifierHelper.getCriticalChance(weapon) + this.getBounceCritChance();

        if (rand.nextFloat() < chance) {
            float critMultiplier = this.modifiedGun.getProjectile().getCritDamageMultiplier() + this.getBounceCritChance();
            return damage * critMultiplier;
        } else {
            return damage;
        }
    }

    private void doTrailParticles(int num, Vec3 startPos, Vec3 endPos) {
        for (float i = 0; i <= num; i++) {
            float t = i / (num - 1);

            double tx = startPos.x + t * (endPos.x - startPos.x);
            double ty = startPos.y + t * (endPos.y - startPos.y);
            double tz = startPos.z + t * (endPos.z - startPos.z);

            this.level().addParticle(CCParticleTypes.EXPOSED_FLOODLIGHT_DUST.get(), true, tx, ty, tz, 0, 0, 0);
        }
    }


    Vec3 oldPos = null;
    @Override
    protected void onProjectileTick() {
        if (this.level().isClientSide && (this.tickCount > 1 && this.tickCount < this.life)) {
            this.level().addParticle(CCParticleTypes.SPARKLER_SPARK.getFirst().get(), true, this.getX(), this.getY(), this.getZ(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
            Vec3 newPos = this.position();

            if (oldPos != null) {
                doTrailParticles(10, oldPos, newPos);
            }
            oldPos = newPos;
        }
    }
}
