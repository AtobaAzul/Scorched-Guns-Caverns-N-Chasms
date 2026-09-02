package net.atobaazul.scguns_cnc.util;

import net.minecraft.world.entity.LivingEntity;
import top.ribs.scguns.entity.projectile.ProjectileEntity;

import javax.annotation.Nullable;

public interface ProjectileEntityExtension {
    void scguns_cnc$setShooter(@Nullable LivingEntity entity);
}
