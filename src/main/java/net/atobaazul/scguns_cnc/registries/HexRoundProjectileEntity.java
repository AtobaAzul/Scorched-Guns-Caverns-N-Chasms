package net.atobaazul.scguns_cnc.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import top.ribs.scguns.Config;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.entity.projectile.AdvancedRoundProjectileEntity;
import top.ribs.scguns.init.ModDamageTypes;
import top.ribs.scguns.init.ModTags;
import top.ribs.scguns.item.GunItem;
import top.ribs.scguns.util.GunEnchantmentHelper;
import top.ribs.scguns.util.GunModifierHelper;

public class HexRoundProjectileEntity extends AdvancedRoundProjectileEntity {
    private static final float ADVANCED_SHIELD_DISABLE_CHANCE = 0.45f;
    private static final float HEADSHOT_EFFECT_DURATION_MULTIPLIER = 1.25f;

    public HexRoundProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun);
    }

    float getCriticalDamage(ItemStack weapon, RandomSource rand, float damage) {
        float chance = GunModifierHelper.getCriticalChance(weapon);
        if (rand.nextFloat() < chance) {
            float critMultiplier = this.modifiedGun.getGeneral().getCritDamageMultiplier();
            return damage * critMultiplier;
        } else {
            return damage;
        }
    }

    @Override
    protected void onHitEntity(Entity entity, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot) {
        float damage = this.getDamage();
        float newDamage = this.getCriticalDamage(this.getWeapon(), this.random, damage);
        boolean critical = damage != newDamage;
        damage = newDamage;
        ResourceLocation advantage = this.getProjectile().getAdvantage();
        damage *= advantageMultiplier(entity);

        boolean wasAlive = entity instanceof LivingEntity && entity.isAlive();
        if (headshot) {
            damage *= Config.COMMON.gameplay.headShotDamageMultiplier.get();
        }

        if (entity instanceof LivingEntity livingTarget) {
            damage = GunEnchantmentHelper.getPuncturingDamageReduction(this.getWeapon(), livingTarget, damage);
            damage = applyProjectileProtection(livingTarget, damage);
            damage = calculateArmorBypassDamage(livingTarget, damage);
        }
        boolean blocked = ProjectileHelper.handleShieldHit(entity, this, damage, ADVANCED_SHIELD_DISABLE_CHANCE);
        if (!blocked) {
            DamageSource source = ModDamageTypes.Sources.projectile(this.level().registryAccess(), this, (LivingEntity) this.getOwner());

            if (!(entity.getType().is(ModTags.Entities.GHOST) && !advantage.equals(ModTags.Entities.UNDEAD.location()))) {
                entity.hurt(source, damage);

                if (entity instanceof LivingEntity livingEntity) {
                    ResourceLocation effectLocation = this.getProjectile().getImpactEffect();
                    if (effectLocation != null) {
                        float effectChance = this.getProjectile().getImpactEffectChance();
                        if (this.random.nextFloat() < effectChance) {
                            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(effectLocation);
                            if (effect != null) {
                                int duration = this.getProjectile().getImpactEffectDuration();
                                if (headshot) {
                                    duration = (int) (duration * HEADSHOT_EFFECT_DURATION_MULTIPLIER);
                                }
                                if (entity instanceof LivingEntity livingTarget) {
                                    damage = applyProjectileProtection(livingTarget, damage);
                                    damage = calculateArmorBypassDamage(livingTarget, damage);
                                }
                                livingEntity.addEffect(new MobEffectInstance(
                                        effect,
                                        duration,
                                        this.getProjectile().getImpactEffectAmplifier()
                                ));
                            }
                        }
                    }
                }
            }

            if (entity instanceof LivingEntity) {
                GunEnchantmentHelper.applyElementalPopEffect(this.getWeapon(), (LivingEntity) entity);
            }
        }

    }
}