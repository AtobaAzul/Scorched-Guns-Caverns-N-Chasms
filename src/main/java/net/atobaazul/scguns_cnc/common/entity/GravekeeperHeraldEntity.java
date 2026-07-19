package net.atobaazul.scguns_cnc.common.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.atobaazul.scguns_cnc.common.entity.ai.HeraldGunAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import top.ribs.scguns.item.GunItem;

import javax.annotation.Nullable;

public class GravekeeperHeraldEntity extends AbstractGravekeeperGunnerEntity implements GeoAnimatable, GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public static final EntityDataAccessor<Byte> DATA_ENRAGED = SynchedEntityData.defineId(GravekeeperHeraldEntity.class, EntityDataSerializers.BYTE);
    private int enrage_timer = 0;

    public GravekeeperHeraldEntity(EntityType<? extends GravekeeperHeraldEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static final RawAnimation ENRAGE = RawAnimation.begin().thenPlay("enrage");
    public static final RawAnimation WALK_ENRAGED = RawAnimation.begin().thenLoop("move.walk.enraged");
    public static final RawAnimation IDLE_ENRAGED = RawAnimation.begin().thenLoop("misc.idle.enraged");

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40D)
                .add(Attributes.ATTACK_DAMAGE, 6.0f)
                .add(Attributes.ARMOR, 18f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.ATTACK_SPEED, 2f)
                .add(Attributes.FOLLOW_RANGE, 48D)
                .add(CCAttributes.LIFESTEAL.get(), 0.5d)

                .build();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        boolean ret = super.hurt(pSource, pAmount);

        if (!isEnraged() && this.getHealth() / this.getMaxHealth() <= 0.5) {
            enrage();
        }

        return ret;
    }


    public void enrage() {
        setEnraged((byte) 1);
        this.enrage_timer = 45; //2.5s, matching anim length

        //TODO: SOUND
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

    @Override
    public void tick() {
        super.tick();
        if (isEnraged() && this.enrage_timer > 0) {
            this.enrage_timer--;
        }

        System.out.println(this.enrage_timer);
        System.out.println(isEnraged());
    }

    public boolean isEnraged() {
        return this.entityData.get(DATA_ENRAGED) == (byte) 1;
    }

    public void setEnraged(byte val) {
        this.entityData.set(DATA_ENRAGED, val);
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
        super.setItemSlot(equipmentSlot, itemStack);
        if (itemStack.getItem() instanceof GunItem) {
            this.goalSelector.removeAllGoals((goal) -> goal instanceof MeleeAttackGoal);
            this.goalSelector.addGoal(1, new HeraldGunAttackGoal<>(this, itemStack, 2.0F, AIType.SMART, 1));
        } else {
            this.goalSelector.removeAllGoals((goal) -> goal instanceof HeraldGunAttackGoal);
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2, false));
        }
    }

    @Override
    public void registerGoals() {
        ItemStack mainHandItem = this.getMainHandItem();
        if (mainHandItem.getItem() instanceof GunItem) {
            this.goalSelector.addGoal(1, new HeraldGunAttackGoal<>(this, mainHandItem, 2.0F, AIType.SMART, 1));
        } else {
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2, false));
        }
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !((Player) player).isCreative() && !player.isSpectator()));

        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, (entity) -> hitList.contains(entity.getClass())));

        this.targetSelector.addGoal(6, new HurtByTargetGoal(this, AbstractGravekeeperGunnerEntity.class).setAlertOthers(AbstractGravekeeperGunnerEntity.class));
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
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        setEnraged(tag.getBoolean("Enraged") ? (byte) 1 : (byte) 0);
    }
}
