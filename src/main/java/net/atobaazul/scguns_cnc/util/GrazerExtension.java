package net.atobaazul.scguns_cnc.util;

import top.ribs.scguns.entity.projectile.ProjectileEntity;

import java.util.Set;

public interface GrazerExtension {
    boolean scguns_cnc$bulletProjectileJustDeflected(ProjectileEntity projectile);
    void scguns_cnc$addDeflectedBulletProjectile(ProjectileEntity projectile);
    void scguns_cnc$updateBulletDeflectProjectiles();
}
