package net.atobaazul.scguns_cnc.common.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.atobaazul.scguns_cnc.ModConfigs;
import net.atobaazul.scguns_cnc.common.entity.ai.AcolyteGunAttackGoal;
import net.atobaazul.scguns_cnc.registries.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
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
import top.ribs.scguns.entity.player.PlayerGunProgression;
import top.ribs.scguns.item.GunItem;

import javax.annotation.Nullable;

public class GravekeeperAcolyteEntity extends AbstractGravekeeperGunnerEntity implements GeoAnimatable, GeoEntity {
    public static final RawAnimation THROW = RawAnimation.begin().thenPlay("throw");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public GravekeeperAcolyteEntity(EntityType<? extends GravekeeperAcolyteEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40D).add(Attributes.ATTACK_DAMAGE, 8.0f).add(Attributes.ARMOR, 25f).add(Attributes.MOVEMENT_SPEED, 0.2f).add(Attributes.ATTACK_SPEED, 1.0f).add(Attributes.FOLLOW_RANGE, 48D).add(CCAttributes.MAGIC_PROTECTION.get(), 1f).build();
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        //check for structures.
        if (!(this.getMainHandItem().getItem() instanceof GunItem)) {
            EntityEquipmentConfig.equipEntity(this, "scguns_cnc:gravekeeper_acolyte");
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public static boolean checkValidProgressionSpawn(ServerLevel level, BlockPos pos) {
        Player nearestPlayer = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 128, false);
        if (nearestPlayer == null) {
            return false;
        }

        PlayerGunProgression progression = PlayerGunProgression.get(nearestPlayer);

        return ModConfigs.COMMON.required_raid_tier.get() <= 0 || progression.getCurrentRaidLevel() >= ModConfigs.COMMON.required_raid_tier.get();
    }

    public static boolean checkAcolyteSpawnRules(EntityType<?> monster, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return !level.getBlockState(pos.below()).is(Blocks.NETHER_WART_BLOCK) && !level.getBlockState(pos.below()).is(Blocks.WARPED_WART_BLOCK) && random.nextFloat() > 0.66 && checkValidProgressionSpawn((ServerLevel) level, pos);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.removeAllGoals(goal -> true);

        ItemStack mainHandItem = this.getMainHandItem();

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AcolyteGunAttackGoal<>(this, mainHandItem, 2.0F, AIType.SMART, 1));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !((Player) player).isCreative() && !player.isSpectator()));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, (entity) -> hitList.stream().anyMatch((clazz) -> clazz.isInstance(entity))));

        this.targetSelector.addGoal(6, new HurtByTargetGoal(this, AbstractGravekeeperGunnerEntity.class).setAlertOthers(AbstractGravekeeperGunnerEntity.class));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(new AnimationController<>(this, "Throw", 1, state -> PlayState.STOP).triggerableAnim("throw", THROW));
    }
}
