package net.atobaazul.scguns_cnc.common.entity;

import net.atobaazul.scguns_cnc.common.entity.ai.GhoulGunAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import top.ribs.scguns.entity.ai.AIType;
import top.ribs.scguns.entity.monster.*;
import top.ribs.scguns.item.GunItem;

public abstract class AbstractGravekeeperGunnerEntity extends Monster implements GeoAnimatable, GeoEntity {
    public static final RawAnimation SHOOT = RawAnimation.begin().thenPlay("shoot");
    public static final RawAnimation GUN_MELEE = RawAnimation.begin().thenPlay("gun_melee");
    public static final RawAnimation MELEE = RawAnimation.begin().thenPlay("melee");

    public static final RawAnimation RELOAD = RawAnimation.begin().thenPlay("reload");
    public static final RawAnimation WALK_ALERT = RawAnimation.begin().thenLoop("move.walk.alert");
    public static final RawAnimation IDLE_ALERT = RawAnimation.begin().thenLoop("misc.idle.alert");

    //melee swordsman anims
    public static final RawAnimation WALK_MELEE_ALERT = RawAnimation.begin().thenLoop("move.walk.alert_melee");
    public static final RawAnimation IDLE_MELEE_ALERT = RawAnimation.begin().thenLoop("misc.idle.alert_melee");
    public static final RawAnimation WALK_MELEE = RawAnimation.begin().thenLoop("move.walk_melee");
    public static final RawAnimation IDLE_MELEE = RawAnimation.begin().thenLoop("misc.idle_melee");


    /*
        Anims

        shoot
        melee
        reload
        move.walk.alert
        move.walk
        misc.idle.alert
        misc.idle
     */

    private static final EntityDataAccessor<Byte> DATA_AGGRO = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BYTE);
    private int ticksUntilNextAlert = 40;

    protected AbstractGravekeeperGunnerEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_AGGRO, (byte) 0);
    }

    @Override
    public void setTarget(LivingEntity entity) {
        super.setTarget(entity);
        updateAggroState();
    }

    public boolean hasAggro() {
        return this.entityData.get(DATA_AGGRO) == (byte) 1;
    }

    public void updateAggroState() {
        this.entityData.set(DATA_AGGRO, this.getTarget() != null ? (byte) 1 : (byte) 0);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        boolean hasMeleeWeapon = !(this.getMainHandItem().getItem() instanceof GunItem);

        controllers.add(new AnimationController<>(this, "Walk/Run/Idle", 2, state -> {
            if (AbstractGravekeeperGunnerEntity.this.swinging) return state.setAndContinue(MELEE);

            if (state.isMoving()) return state.setAndContinue(hasAggro() ? hasMeleeWeapon ? WALK_MELEE_ALERT : WALK_ALERT : hasMeleeWeapon ? WALK_MELEE : DefaultAnimations.WALK);

            return state.setAndContinue(hasAggro() ? hasMeleeWeapon ? IDLE_MELEE_ALERT : IDLE_ALERT : hasMeleeWeapon ? IDLE_MELEE : DefaultAnimations.IDLE);
        }));


        controllers.add(new AnimationController<>(this, "Gun Melee", 1, state -> PlayState.STOP).triggerableAnim("gun_melee", GUN_MELEE));

        controllers.add(new AnimationController<>(this, "Shoot", 0, state -> PlayState.STOP).triggerableAnim("shoot", SHOOT));
        controllers.add(new AnimationController<>(this, "Reload", 1, state -> PlayState.STOP).triggerableAnim("reload", RELOAD));
    }

    @Override
    public void tick() {
        super.tick();
        tryAlertAllies();
    }

    private void tryAlertAllies() {
        if (this.ticksUntilNextAlert > 0) {
            --this.ticksUntilNextAlert;
        } else {
            //if (this.getSensing().hasLineOfSight(this.getTarget())) {
                this.alertAllies();
            //}
            this.ticksUntilNextAlert = 20 + this.random.nextInt(20);
        }
    }

    private static final double ALLIANCE_RANGE = 32.0;
    private static final int ALERT_RANGE_Y = 10;

    private void alertAllies() {
        AABB alertArea = AABB.unitCubeFromLowerCorner(this.position()).inflate(ALLIANCE_RANGE, ALERT_RANGE_Y, ALLIANCE_RANGE);
        if (this.getTarget() != null) {
            this.level().getEntitiesOfClass(AbstractGravekeeperGunnerEntity.class, alertArea, EntitySelector.NO_SPECTATORS)
                    .stream()
                    .filter(entity -> entity != this)
                    .filter(entity -> entity.getTarget() == null)
                    .filter(entity -> !entity.isAlliedTo(this.getTarget()))
                    .forEach(entity -> entity.setTarget(this.getTarget()));
        }
    }


    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("AlertCooldown", this.ticksUntilNextAlert);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.ticksUntilNextAlert = tag.getInt("AlertCooldown");
    }


    @Override
    public void registerGoals() {
        ItemStack mainHandItem = this.getMainHandItem();

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GhoulGunAttackGoal<>(this, mainHandItem, 2.0F, AIType.SMART, 1));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !((Player) player).isCreative() && !player.isSpectator()));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, AdjudicatorEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, DissidentEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, CogKnightEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, CogMinionEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, PraetorEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, ScampTankEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, SubjugatorEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, SkyCarrierEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, SupplyScampEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, TraumaUnitEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, SignalBeaconEntity.class, false));

        this.targetSelector.addGoal(6, new HurtByTargetGoal(this, AbstractGravekeeperGunnerEntity.class).setAlertOthers(AbstractGravekeeperGunnerEntity.class));
    }
}
