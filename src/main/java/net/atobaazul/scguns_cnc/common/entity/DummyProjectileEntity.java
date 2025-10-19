package net.atobaazul.scguns_cnc.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.item.GunItem;

public class DummyProjectileEntity extends ProjectileEntity {
    public DummyProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn) {
        super(entityType, worldIn);
        this.remove(RemovalReason.DISCARDED);
    }

    public DummyProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public void tick() {
        this.remove(RemovalReason.DISCARDED);
    }
}