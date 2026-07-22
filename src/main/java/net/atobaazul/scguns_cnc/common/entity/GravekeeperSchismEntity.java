package net.atobaazul.scguns_cnc.common.entity;

import net.atobaazul.scguns_cnc.common.entity.ai.SchismGunAttackGoal;
import net.atobaazul.scguns_cnc.registries.ModSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ribs.scguns.config.EntityEquipmentConfig;
import top.ribs.scguns.entity.ai.AIType;

import javax.annotation.Nullable;

public class GravekeeperSchismEntity extends AbstractGravekeeperGunnerEntity implements GeoAnimatable, GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public static final RawAnimation THROW = RawAnimation.begin().thenPlay("throw");
    public static final RawAnimation OFFHAND_MELEE = RawAnimation.begin().thenPlay("offhand_melee");

    public GravekeeperSchismEntity(EntityType<? extends GravekeeperSchismEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30D).add(Attributes.ATTACK_DAMAGE, 8f).add(Attributes.ARMOR, 12f).add(Attributes.MOVEMENT_SPEED, 0.2f).add(Attributes.ATTACK_SPEED, 2f).add(Attributes.FOLLOW_RANGE, 48D).build();
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.GRAVEKEEPER_GHOUL_AMBIENT.get();
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(new AnimationController<>(this, "Throw", 1, state -> PlayState.STOP).triggerableAnim("throw", THROW));
        controllers.add(new AnimationController<>(this, "Offhand Melee", 1, state -> PlayState.STOP).triggerableAnim("offhand_melee", OFFHAND_MELEE));

    }

    @Override
    public void registerGoals() {
        this.goalSelector.removeAllGoals(goal -> true);

        ItemStack mainHandItem = this.getMainHandItem();
        this.goalSelector.addGoal(1, new SchismGunAttackGoal<>(this, mainHandItem, 1.8F, AIType.SMART, 1));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !((Player) player).isCreative() && !player.isSpectator()));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, (entity) -> hitList.stream().anyMatch((clazz) -> clazz.isInstance(entity))));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this, AbstractGravekeeperGunnerEntity.class).setAlertOthers(AbstractGravekeeperGunnerEntity.class));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        EntityEquipmentConfig.equipEntity(this, "scguns_cnc:gravekeeper_schism");

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
}
