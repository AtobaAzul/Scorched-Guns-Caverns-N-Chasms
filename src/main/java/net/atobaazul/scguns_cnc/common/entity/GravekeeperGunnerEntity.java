package net.atobaazul.scguns_cnc.common.entity;

import net.atobaazul.scguns_cnc.common.entity.ai.GhoulGunAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import top.ribs.scguns.entity.ai.AIType;

public abstract class GravekeeperGunnerEntity extends Monster implements GeoAnimatable, GeoEntity {
    public static final RawAnimation SHOOT = RawAnimation.begin().thenPlay("shoot");
    public static final RawAnimation MELEE = RawAnimation.begin().thenPlay("melee");
    public static final RawAnimation RELOAD = RawAnimation.begin().thenPlay("reload");
    public static final RawAnimation WALK_ALERT = RawAnimation.begin().thenLoop("move.walk.alert");
    public static final RawAnimation IDLE_ALERT = RawAnimation.begin().thenLoop("misc.idle.alert");

    private static final EntityDataAccessor<Byte> DATA_AGGRO = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BYTE);

    protected GravekeeperGunnerEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
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
        controllers.add(new AnimationController<>(this, "Walk/Run/Idle", 2, state -> {
            if (state.isMoving()) return state.setAndContinue(hasAggro() ? WALK_ALERT : DefaultAnimations.WALK);

            return state.setAndContinue(hasAggro() ? IDLE_ALERT : DefaultAnimations.IDLE);
        }));

        controllers.add(new AnimationController<>(this, "Melee", 1, state -> PlayState.STOP).triggerableAnim("melee", MELEE));

        controllers.add(new AnimationController<>(this, "Shoot", 0, state -> PlayState.STOP).triggerableAnim("shoot", SHOOT));
        controllers.add(new AnimationController<>(this, "Reload", 1, state -> PlayState.STOP).triggerableAnim("reload", RELOAD));

    }

    @Override
    public void registerGoals() {
        ItemStack mainHandItem = this.getMainHandItem();

        this.goalSelector.addGoal(1, new GhoulGunAttackGoal<>(this, mainHandItem, 2.0F, AIType.SMART, 1));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !((Player) player).isCreative() && !player.isSpectator()));
        this.targetSelector.addGoal(6, new HurtByTargetGoal(this, GravekeeperGunnerEntity.class).setAlertOthers(GravekeeperGunnerEntity.class));
    }
}
