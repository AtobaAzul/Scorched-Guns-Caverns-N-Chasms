package net.atobaazul.scguns_cnc.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import top.ribs.scguns.entity.ai.AIType;
import top.ribs.scguns.entity.ai.GunAttackGoal;
import top.ribs.scguns.item.GunItem;

public abstract class GravekeeperGunnerEntity extends Monster implements GeoAnimatable, GeoEntity {
    protected GravekeeperGunnerEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }


    /*TODO
           The overall goal for animation/AI is to have the mobs be deadlier and less chaotic.

           They'll walk up to a player, and shoot a burst, with a chance of the burst being an accurate one
           where the mob visually aims down their gun.

           Also standard walk/run/idle.
           And a alert walk when they're following the player.
     */
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Run/Idle", state -> {
            if (state.isMoving()) return state.setAndContinue(DefaultAnimations.WALK);

            return state.setAndContinue(DefaultAnimations.IDLE);
        }));
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

        boolean hasGun = mainHandItem.getItem() instanceof GunItem;

        if (hasGun) {
            this.goalSelector.addGoal(1, new GunAttackGoal<>(this, mainHandItem, 1.0F, AIType.TACTICAL, 3));
        }

        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !((Player) player).isCreative() && !player.isSpectator()));

    }

}
