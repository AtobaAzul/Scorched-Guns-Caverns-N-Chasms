package net.atobaazul.scguns_cnc.common.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCAttributes;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.atobaazul.scguns_cnc.registries.ModSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ribs.scguns.item.GunItem;

import javax.annotation.Nullable;
import java.util.List;

public class GravekeeperGhoulEntity extends AbstractGravekeeperGunnerEntity implements GeoAnimatable, GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public GravekeeperGhoulEntity(EntityType<? extends GravekeeperGhoulEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.ATTACK_DAMAGE, 8.0f)
                .add(Attributes.ARMOR, 18f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.FOLLOW_RANGE, 48D)
                .add(CCAttributes.MAGIC_PROTECTION.get(), 1f)
                .build();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    public List<RegistryObject<? extends GunItem>> availableGuns = List.of(
            ModItems.HANGMAN_CARBINE,
            ModItems.NECROSIS,
            ModItems.GALLOWS
    );

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.GRAVEKEEPER_GHOUL_AMBIENT.get();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        GunItem selectedGun = availableGuns.get((int) Math.floor(Math.random() * availableGuns.size())).get();

        this.setItemSlot(EquipmentSlot.MAINHAND, selectedGun.getDefaultInstance());
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }


}
