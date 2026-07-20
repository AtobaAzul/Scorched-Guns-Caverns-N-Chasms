package net.atobaazul.scguns_cnc.common.entity;

import net.atobaazul.scguns_cnc.common.entity.ai.GhoulGunAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ribs.scguns.config.EntityEquipmentConfig;
import top.ribs.scguns.entity.ai.AIType;
import top.ribs.scguns.item.GunItem;

import javax.annotation.Nullable;

public class GravekeeperNeophyteEntity extends AbstractGravekeeperGunnerEntity implements GeoAnimatable, GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private boolean reassesedGoals = false;

    public GravekeeperNeophyteEntity(EntityType<? extends GravekeeperNeophyteEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20D).add(Attributes.ATTACK_DAMAGE, 1.0f).add(Attributes.ARMOR, 7f).add(Attributes.MOVEMENT_SPEED, 0.2f).add(Attributes.ATTACK_SPEED, 2f).add(Attributes.FOLLOW_RANGE, 48D).build();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (!reassesedGoals) {
                registerCustomGoals();
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
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
    public void registerGoals() {
    }

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
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !((Player) player).isCreative() && !player.isSpectator()));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, (entity) -> hitList.contains(entity.getClass())));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this, AbstractGravekeeperGunnerEntity.class).setAlertOthers(AbstractGravekeeperGunnerEntity.class));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        EntityEquipmentConfig.equipEntity(this, "scguns_cnc:gravekeeper_neophyte");

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
}
