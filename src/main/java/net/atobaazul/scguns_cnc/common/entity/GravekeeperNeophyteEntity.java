package net.atobaazul.scguns_cnc.common.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.atobaazul.scguns_cnc.common.entity.ai.GhoulGunAttackGoal;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ribs.scguns.entity.ai.AIType;
import top.ribs.scguns.item.GunItem;

import javax.annotation.Nullable;
import java.util.List;

public class GravekeeperNeophyteEntity extends AbstractGravekeeperGunnerEntity implements GeoAnimatable, GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public List<RegistryObject<? extends Item>> availableGuns;

    public GravekeeperNeophyteEntity(EntityType<? extends GravekeeperNeophyteEntity> entityType, Level level) {
        super(entityType, level);
        availableGuns = List.of(
                ModItems.MORTICIAN,
                CCItems.SILVER_AXE,
                CCItems.SILVER_SWORD
        );
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.ATTACK_DAMAGE, 1.0f)
                .add(Attributes.ARMOR, 7f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.ATTACK_SPEED, 2f)
                .add(Attributes.FOLLOW_RANGE, 48D)
                .build();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        super.setItemSlot(equipmentSlot, itemStack);
        if (itemStack.getItem() instanceof  GunItem) {
            this.goalSelector.removeAllGoals((goal) ->  goal instanceof MeleeAttackGoal);
            this.goalSelector.addGoal(1, new GhoulGunAttackGoal<>(this, itemStack, 2.0F, AIType.RECKLESS, 1));
        } else {
            this.goalSelector.removeAllGoals((goal) ->  goal instanceof GhoulGunAttackGoal);
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, false));
        }
    }

    @Override
    public void registerGoals() {
        ItemStack mainHandItem = this.getMainHandItem();
        if (mainHandItem.getItem() instanceof GunItem) {
            this.goalSelector.addGoal(1, new GhoulGunAttackGoal<>(this, mainHandItem, 2.0F, AIType.RECKLESS, 1));
        } else {
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, false));
        }
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true, player -> !((Player) player).isCreative() && !player.isSpectator()));
        this.targetSelector.addGoal(6, new HurtByTargetGoal(this, AbstractGravekeeperGunnerEntity.class).setAlertOthers(AbstractGravekeeperGunnerEntity.class));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        Item selectedWeapon = this.availableGuns.get((int) Math.floor(Math.random() * this.availableGuns.size())).get();
        this.setItemSlot(EquipmentSlot.MAINHAND, selectedWeapon.getDefaultInstance());

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
}
