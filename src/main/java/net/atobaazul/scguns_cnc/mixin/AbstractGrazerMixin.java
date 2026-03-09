package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer.AbstractGrazer;
import net.atobaazul.scguns_cnc.util.GrazerExtension;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import top.ribs.scguns.entity.projectile.ProjectileEntity;

import java.util.HashSet;
import java.util.Set;

@Mixin(AbstractGrazer.class)
public abstract class AbstractGrazerMixin extends Animal implements GrazerExtension {
    @Unique
    public long scguns_cnc$lastBulletDeflectTick;
    @Unique
    private Set<ProjectileEntity> scguns_cnc$bulletDeflectedThisTick = new HashSet<>();
    @Unique
    private Set<ProjectileEntity> scguns_cnc$bulletDeflectedPrevTick = new HashSet<>();

    protected AbstractGrazerMixin(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    @Shadow
    public abstract double shellRadius();

    @Shadow
    public abstract double shellCenterY(float partialTick);

    @Shadow
    public abstract double shellCenterZ(float partialTick);

    @Override
    public void scguns_cnc$updateBulletDeflectProjectiles() {
        long l = this.level().getGameTime();

        if (this.scguns_cnc$lastBulletDeflectTick != l) {
            this.scguns_cnc$bulletDeflectedPrevTick = this.scguns_cnc$bulletDeflectedThisTick;
            this.scguns_cnc$bulletDeflectedThisTick = new HashSet<>();
            this.scguns_cnc$lastBulletDeflectTick = l;
        }
    }

    @Override
    public boolean scguns_cnc$bulletProjectileJustDeflected(ProjectileEntity projectile) {
        this.scguns_cnc$updateBulletDeflectProjectiles();
        return this.scguns_cnc$bulletDeflectedPrevTick.contains(projectile);
    }

    @Override
    public void scguns_cnc$addDeflectedBulletProjectile(ProjectileEntity projectile) {
        this.scguns_cnc$bulletDeflectedThisTick.add(projectile);
    }

    @WrapMethod(method = "tick")
    public void scguns_cnc$tick(Operation<Void> original) {
        original.call();
        scguns_cnc$updateBulletDeflectProjectiles();

        double d0 = shellRadius();
        Vec3 vec3 = new Vec3(0.0D, shellCenterY(1.0F), shellCenterZ(1.0F)).yRot(-this.getYRot() * Mth.DEG_TO_RAD);
        AABB aabb = new AABB(-d0, -d0, -d0, d0, d0, d0).move(this.position()).move(vec3).inflate(0.3D);
        for (ProjectileEntity projectile : this.level().getEntitiesOfClass(ProjectileEntity.class, aabb)) {
            if (this.scguns_cnc$bulletDeflectedPrevTick.contains(projectile)) {
                this.scguns_cnc$bulletDeflectedThisTick.add(projectile);
            }
        }
    }
}
