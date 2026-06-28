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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    public static final RawAnimation WALK_ALERT = RawAnimation.begin().thenLoop("move.walk.alert");
    public static final RawAnimation IDLE_ALERT = RawAnimation.begin().thenLoop("misc.idle.alert");
    private static final EntityDataAccessor<Byte> DATA_AGGRO = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BYTE);
    protected GravekeeperGunnerEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    /*TODO
           The overall goal for animation/AI is to have the mobs be deadlier and less chaotic.

           They'll walk up to a player, and shoot a burst, with a chance of the burst being an accurate one
           where the mob visually aims down their gun.

           Also standard walk/run/idle.
           And a alert walk when they're following the player.
     */

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
        this.entityData.set(DATA_AGGRO,  entity != null ? (byte) 1 : (byte) 0);
    }

    private boolean hasAggro() {
        return this.entityData.get(DATA_AGGRO) == (byte) 1;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Run/Idle", 2, state -> {
            if (state.isMoving())
                return state.setAndContinue(hasAggro() ? WALK_ALERT : DefaultAnimations.WALK);

            return state.setAndContinue(hasAggro() ? IDLE_ALERT : DefaultAnimations.IDLE);
        }));

        controllers.add(new AnimationController<>(this, "Shoot", 0, state -> PlayState.STOP).triggerableAnim("shoot", SHOOT));
    }


    /*
    TODO:
       I don't want to use scguns's gun attack goal. no likey.

       One that follows this loop:
        -> try to approach player
          -> if player within range
              -> shoot or aim shoot. Burst length depends on nearby friendlies.
              -> circle around.
          -> if timeout and player is in sight (player too far/escaping)
              -> aim shoot or run
     */
    @Override
    public void registerGoals() {
        ItemStack mainHandItem = this.getMainHandItem();


        this.goalSelector.addGoal(1, new GhoulGunAttackGoal<>(this, mainHandItem, 2.0F, AIType.SMART, 1));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !((Player) player).isCreative() && !player.isSpectator()));

    }

}
