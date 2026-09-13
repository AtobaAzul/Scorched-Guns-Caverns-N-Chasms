package net.atobaazul.scguns_cnc.common.entity.projectile.throwable;

import com.mrcrayfish.framework.api.network.LevelLocation;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import net.atobaazul.scguns_cnc.common.entity.projectile.EnergyBoltProjectileEntity;
import net.atobaazul.scguns_cnc.registries.ModEntities;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.atobaazul.scguns_cnc.registries.ModParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.ribs.scguns.Config;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.ProjectileManager;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.entity.throwable.ThrowableGrenadeEntity;
import top.ribs.scguns.interfaces.IProjectileFactory;
import top.ribs.scguns.item.GunItem;
import top.ribs.scguns.network.PacketHandler;
import top.ribs.scguns.network.message.S2CMessageBulletTrail;
import top.ribs.scguns.util.GunEnchantmentHelper;

import static net.atobaazul.scguns_cnc.registries.ModSoundEvents.ELECTROTHERMAL_AUTOCANNON_FIRE;
import static net.atobaazul.scguns_cnc.registries.ModSoundEvents.MALISON_EXPLOSION;

public class ThrowablePlasmaBursterEntity extends ThrowableGrenadeEntity {
    private static final float EXPLOSION_RADIUS = 5.0f;
    public float rotation;
    public float prevRotation;

    public ThrowablePlasmaBursterEntity(EntityType<? extends ThrowableGrenadeEntity> entityType, Level world) {
        super(entityType, world);
        this.setItem(new ItemStack(ModItems.PLASMA_BURSTER.get()));
        this.setMaxLife(40);
    }

    public ThrowablePlasmaBursterEntity(EntityType<? extends ThrowableGrenadeEntity> entityType, Level world, LivingEntity player) {
        super(entityType, world, player);
        this.setItem(new ItemStack(ModItems.PLASMA_BURSTER.get()));
        this.setMaxLife(40);
    }

    public ThrowablePlasmaBursterEntity(Level world, LivingEntity player, int maxCookTime) {
        super(ModEntities.THROWABLE_PLASMA_BURSTER.get(), world, player);
        this.setItem(new ItemStack(ModItems.PLASMA_BURSTER.get()));
        this.setMaxLife(40);
        this.setShouldBounce(true);
    }

    private static void sendProjectileTrail(ServerPlayer player, ProjectileEntity[] projectiles, Gun.Projectile projectileProps, boolean b) {
        if (projectileProps.shouldHideTrail()) {
            return;
        }

        double spawnX = player.getX();
        double spawnY = player.getY() + 1.0;
        double spawnZ = player.getZ();
        double radius = Config.COMMON.network.projectileTrackingRange.get();
        ParticleOptions data = GunEnchantmentHelper.getParticle(player.getMainHandItem());

        S2CMessageBulletTrail messageBulletTrail = new S2CMessageBulletTrail(projectiles, projectileProps, player.getId(), data, true);

        PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(player.level(), spawnX, spawnY, spawnZ, radius), messageBulletTrail);
    }

    private static Vec3 generateRandomDirection(net.minecraft.util.RandomSource random) {
        float x, y, z;
        float lengthSquared;

        do {
            x = random.nextFloat() * 2.0f - 1.0f;
            y = random.nextFloat() * 2.0f - 1.0f;
            z = random.nextFloat() * 2.0f - 1.0f;
            lengthSquared = x * x + y * y + z * z;
        } while (lengthSquared > 1.0f || lengthSquared < 0.001f);

        float length = net.minecraft.util.Mth.sqrt(lengthSquared);
        return new Vec3(x / length, y / length, z / length);
    }

    private void playExplosionSound(Level world, Vec3 pos, Double radius) {
        float volume = (float) Math.min(1.0F, radius * 0.5F);
        float pitch = 0.9F + world.random.nextFloat() * 0.3F;

        //TODO: Explode sound
        world.playSound(null, pos.x, pos.y, pos.z, ELECTROTHERMAL_AUTOCANNON_FIRE.get(), SoundSource.BLOCKS, volume, pitch);
    }

    protected void defineSynchedData() {
    }

    public void doProjectileBurst(Entity entity) {
        if (entity.level().isClientSide()) {
            return;
        }

        Level world = entity.level();

        ProjectileEntity[] spawnedProjectiles = new ProjectileEntity[15];
        GunItem gun = ModItems.SCATTERER.get();
        ItemStack gunStack = new ItemStack(gun);
        Gun modifiedGun = gun.getGun();
        Gun.Projectile projectileProps = modifiedGun.getProjectile(gunStack);

        for (int i = 0; i < 15; i++) {
            Vec3 dir = generateRandomDirection(world.getRandom());

            IProjectileFactory factory = ProjectileManager.getInstance().getFactory(ModItems.PULSE_CORE.getId());
            EnergyBoltProjectileEntity projectileEntity = (EnergyBoltProjectileEntity) factory.create(world, (LivingEntity) this.getOwner(), gunStack, ModItems.SCATTERER.get(), modifiedGun);

            projectileEntity.setWeapon(gunStack);
            projectileEntity.setAdditionalDamage(18);
            projectileEntity.setPos(this.position().add(0, 1, 0));
            projectileEntity.setDeltaMovement(dir.scale(3));
            projectileEntity.setMaxLife(1);
            world.addFreshEntity(projectileEntity);

            spawnedProjectiles[i] = projectileEntity;
            projectileEntity.tick();
        }

        playExplosionSound(world, this.position(), Config.COMMON.grenades.explosionRadius.get());
        sendProjectileTrail((ServerPlayer) this.getOwner(), spawnedProjectiles, projectileProps, false);
    }

    @Override
    public void tick() {
        if (this.tickCount % 2 == 0 && this.tickCount > 20) {
            doProjectileBurst(this);
        }

        super.tick();
        this.prevRotation = this.rotation;
        this.particleTick();
    }

    public void particleTick() {
        if (this.level().isClientSide) {
            this.level().addParticle(ModParticleTypes.ENERGY_BOLT_TRAIL.get(), true, this.getX(), this.getY() + (double) 0.25F, this.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    public void onDeath() {
        ItemEntity grenadeItem = new ItemEntity(this.level(), this.getX(), this.getY() + 1.5, this.getZ(), new ItemStack(ModItems.DEPLETED_PLASMA_BURSTER.get()));
        grenadeItem.setPickUpDelay(20);
        grenadeItem.setDeltaMovement(0.0, 0.2, 0.0);
        this.level().addFreshEntity(grenadeItem);
    }
}
