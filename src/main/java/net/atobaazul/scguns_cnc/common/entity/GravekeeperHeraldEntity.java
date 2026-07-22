package net.atobaazul.scguns_cnc.common.entity;

import net.atobaazul.scguns_cnc.common.entity.ai.GhoulGunAttackGoal;
import net.atobaazul.scguns_cnc.registries.ModEntities;
import net.atobaazul.scguns_cnc.registries.ModSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ribs.scguns.config.EntityEquipmentConfig;
import top.ribs.scguns.entity.ai.AIType;
import top.ribs.scguns.init.ModEffects;
import top.ribs.scguns.item.GunItem;

import javax.annotation.Nullable;

public class GravekeeperHeraldEntity extends AbstractGravekeeperGunnerEntity implements GeoAnimatable, GeoEntity {
    public static final EntityDataAccessor<Byte> DATA_ENRAGED = SynchedEntityData.defineId(GravekeeperHeraldEntity.class, EntityDataSerializers.BYTE);
    public static final RawAnimation ENRAGE = RawAnimation.begin().thenPlay("enrage");
    public static final RawAnimation WALK_ENRAGED = RawAnimation.begin().thenLoop("move.walk.enraged");
    public static final RawAnimation IDLE_ENRAGED = RawAnimation.begin().thenLoop("misc.idle.enraged");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    boolean summonedRaidParty = false;
    private int enrage_timer = 0;
    private boolean reassesedGoals = false;

    public GravekeeperHeraldEntity(EntityType<? extends GravekeeperHeraldEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60D).add(Attributes.KNOCKBACK_RESISTANCE, 0.33f).add(Attributes.ATTACK_DAMAGE, 6.0f).add(Attributes.ARMOR, 18f).add(Attributes.MOVEMENT_SPEED, 0.2f).add(Attributes.ATTACK_SPEED, 2f).add(Attributes.FOLLOW_RANGE, 48D).build();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        boolean ret = super.hurt(pSource, pAmount);

        if (!isEnraged() && this.getHealth() / this.getMaxHealth() <= 0.25) {
            enrage();
        }

        return ret;
    }

    @Override
    protected void setItemSlotAndDropWhenKilled(EquipmentSlot pSlot, ItemStack pStack) {
        super.setItemSlotAndDropWhenKilled(pSlot, pStack);
    }

    public void enrage() {
        setEnraged((byte) 1);
        this.enrage_timer = 45; //2.5s, matching anim length

        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 45, 5, false, false));
        this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 45, 5, false, false));

        if (this.level().getRandom().nextFloat() < this.handDropChances[EquipmentSlot.MAINHAND.getIndex()]) {
            this.spawnAtLocation(this.getMainHandItem());
        }

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_BREAK, SoundSource.HOSTILE, 2.0F, 1F);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundEvents.GRAVEKEEPER_HERALD_ENRAGE.get(), SoundSource.HOSTILE, 0.33F, 0.5F);

        this.setItemSlot(EquipmentSlot.MAINHAND, Items.AIR.getDefaultInstance());
        this.triggerAnim("Enrage", "enrage");
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.enrage_timer > 0;
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ENRAGED, (byte) 0);
    }

    public void summonRaidParty() {
        summonedRaidParty = true;

        for (int i = 0; i <= 5; i++) {
            GravekeeperNeophyteEntity neophyte = ModEntities.GRAVEKEEPER_NEOPHYTE.get().create(this.level());
            neophyte.setPos(this.position());
            ServerLevel serverLevel = (ServerLevel) this.level();
            neophyte.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.PATROL, null, null);
            serverLevel.addFreshEntity(neophyte);
        }

        for (int i = 0; i < 3; i++) {
            GravekeeperGhoulEntity ghoul = ModEntities.GRAVEKEEPER_GHOUL.get().create(this.level());
            ghoul.setPos(this.position());
            ServerLevel serverLevel = (ServerLevel) this.level();
            ghoul.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.PATROL, null, null);
            serverLevel.addFreshEntity(ghoul);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (isEnraged() && this.enrage_timer > 0) {
                this.enrage_timer--;
            }

            this.setSprinting(isEnraged() && !isImmobile() && !swinging && (this.getDeltaMovement().x > 0 || this.getDeltaMovement().z > 0));

            if (!reassesedGoals) {
                registerCustomGoals();
            }
            if (isRaidBoss() && !summonedRaidParty) {
                summonRaidParty();
            }
        }
    }

    public boolean isRaidBoss() {
        return this.getTags().stream().anyMatch(tag -> tag.equals("RaidBoss"));
    }

    public boolean isEnraged() {
        return this.entityData.get(DATA_ENRAGED) == (byte) 1;
    }

    public void setEnraged(byte val) {
        this.entityData.set(DATA_ENRAGED, val);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.GRAVEKEEPER_HERALD_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSoundEvents.GRAVEKEEPER_HERALD_HURT.get();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean ret = super.doHurtTarget(entity);

        if (entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModEffects.LACERATED.get(), 100, 0, false, true));
        }

        return ret;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.GRAVEKEEPER_HERALD_DEATH.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Run/Idle", 2, state -> {
            boolean isEnraged = isEnraged();

            if (GravekeeperHeraldEntity.this.swinging) return state.setAndContinue(MELEE);

            if (state.isMoving())
                return state.setAndContinue(isEnraged ? WALK_ENRAGED : hasAggro() ? WALK_ALERT : DefaultAnimations.WALK);

            return state.setAndContinue(isEnraged ? IDLE_ENRAGED : hasAggro() ? IDLE_ALERT : DefaultAnimations.IDLE);
        }));


        controllers.add(new AnimationController<>(this, "Gun Melee", 1, state -> PlayState.STOP).triggerableAnim("gun_melee", GUN_MELEE));

        controllers.add(new AnimationController<>(this, "Shoot", 0, state -> PlayState.STOP).triggerableAnim("shoot", SHOOT));
        controllers.add(new AnimationController<>(this, "Reload", 1, state -> PlayState.STOP).triggerableAnim("reload", RELOAD));
        controllers.add(new AnimationController<>(this, "Enrage", 1, state -> PlayState.STOP).triggerableAnim("enrage", ENRAGE));
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        ItemStack oldItem = this.getMainHandItem();
        super.setItemSlot(equipmentSlot, itemStack);
        if (equipmentSlot.equals(EquipmentSlot.MAINHAND) && !itemStack.is(oldItem.getItem()) && reassesedGoals) {
            this.reassesedGoals = false;
        }
    }

    @Override
    public void registerGoals() {}


    //@Override
    //not using standard register goals because it runs before mainhand items and pretty much everything else is setup.
    //so we run it once when the entity ticks instead.
    public void registerCustomGoals() {
        this.reassesedGoals = true;
        this.goalSelector.removeAllGoals(goal -> true);

        ItemStack mainHandItem = this.getMainHandItem();
        if (mainHandItem.getItem() instanceof GunItem) {
            this.goalSelector.addGoal(1, new GhoulGunAttackGoal<>(this, mainHandItem, 2.0F, AIType.SMART, 1));
        } else {
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2, false));
        }

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, isEnraged() ? 2.0D : 0.9D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !((Player) player).isCreative() && !player.isSpectator()));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, (entity) -> hitList.stream().anyMatch((clazz) -> clazz.isInstance(entity))));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this, AbstractGravekeeperGunnerEntity.class).setAlertOthers(AbstractGravekeeperGunnerEntity.class));

    }


    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        EntityEquipmentConfig.equipEntity(this, "scguns_cnc:gravekeeper_herald");

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Enraged", isEnraged());
        tag.putBoolean("SummonedRaidParty", summonedRaidParty);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        setEnraged(tag.getBoolean("Enraged") ? (byte) 1 : (byte) 0);
        summonedRaidParty = tag.getBoolean("SummonedRaidParty");
    }
}
