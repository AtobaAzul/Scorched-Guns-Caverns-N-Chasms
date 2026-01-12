package top.ribs.scguns.common.item.gun;

import com.mrcrayfish.framework.api.network.LevelLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import top.ribs.scguns.init.ModSyncedDataKeys;
import top.ribs.scguns.item.GunItem;
import top.ribs.scguns.item.animated.AnimatedGunItem;
import top.ribs.scguns.item.exosuit.ExoSuitCoreItem;
import top.ribs.scguns.network.PacketHandler;
import top.ribs.scguns.network.message.S2CMessageReload;
import top.ribs.scguns.network.message.S2CMessageUpdateAmmo;
import top.ribs.scguns.util.GunModifierHelper;

import javax.annotation.Nullable;
import java.util.List;

//You might be asking yourself "why is this file not under the net.atobaazul.scguns_cnc package?!"
//Well, that's because of several things regarding reload and casing ejection packets EXPLICITLY requiring that the
//gun item's class be under the 'top.ribs.scguns' packages! FOR SOME REASON!!!

public class RechargeableEnergyGunItem extends AnimatedGunItem implements GeoAnimatable, GeoItem {
    private final int energyRequired;
    private final int refillCooldown;
    private final int maxEnergy;
    private final float reloadRechargeTimeMult;
    private final boolean useFireRateRampUp;

    public RechargeableEnergyGunItem(Properties properties, String path, SoundEvent reloadSoundMagOut, SoundEvent reloadSoundMagIn, SoundEvent reloadSoundEnd, SoundEvent boltPullSound, SoundEvent boltReleaseSound, int energyRequired, int refillCooldown, int maxEnergy, float reloadRechargeTimeMult) {
        super(properties, path, reloadSoundMagOut, reloadSoundMagIn, reloadSoundEnd, boltPullSound, boltReleaseSound);
        this.energyRequired = energyRequired;
        this.refillCooldown = refillCooldown;
        this.maxEnergy = maxEnergy;
        this.reloadRechargeTimeMult = reloadRechargeTimeMult;
        this.useFireRateRampUp = false;
    }

    public RechargeableEnergyGunItem(Properties properties, String path, SoundEvent reloadSoundMagOut, SoundEvent reloadSoundMagIn, SoundEvent reloadSoundEnd, SoundEvent boltPullSound, SoundEvent boltReleaseSound, int energyRequired, int refillCooldown, int maxEnergy, float reloadRechargeTimeMult, boolean useFireRateRampUp) {
        super(properties, path, reloadSoundMagOut, reloadSoundMagIn, reloadSoundEnd, boltPullSound, boltReleaseSound);
        this.energyRequired = energyRequired;
        this.refillCooldown = refillCooldown;
        this.maxEnergy = maxEnergy;
        this.reloadRechargeTimeMult = reloadRechargeTimeMult;
        this.useFireRateRampUp = useFireRateRampUp;
    }

    public boolean getUseFireRateRampUp() {
        return this.useFireRateRampUp;
    }

    public float getReloadRechargeTimeMult() {
        return this.reloadRechargeTimeMult;
    }

    public int getEnergyRequired() {
        return this.energyRequired;
    }

    public int getRefillCooldown() {
        return this.refillCooldown;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> new ExoSuitCoreItem.SimpleExoSuitEnergyStorage(stack, maxEnergy));

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
                return cap == ForgeCapabilities.ENERGY ? energy.cast() : LazyOptional.empty();
            }
        };
    }


    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world instanceof ServerLevel) {
            CompoundTag tag = stack.getOrCreateTag();
            int currentAmmo = tag.getInt("AmmoCount");
            int counter = tag.getInt("RechargeCounter");

            if (useFireRateRampUp && entity.tickCount % 10 == 0) {
                int shotCount = tag.getInt("ShotCount");
                tag.putInt("ShotCount", Math.max(0, (int) shotCount - 1));
            }

            LazyOptional<IEnergyStorage> capability = stack.getCapability(ForgeCapabilities.ENERGY);

            GunItem gun = (GunItem) stack.getItem();


            int maxAmmo = GunModifierHelper.getModifiedAmmoCapacity(stack, gun.getModifiedGun(stack));


            int energyStored = capability.map(IEnergyStorage::getEnergyStored).orElse(0);
            if (energyStored >= energyRequired && currentAmmo < maxAmmo && !tag.getBoolean("IsShooting")) {
                tag.putInt("RechargeCounter", counter - 1);

                if (counter <= 0) {
                    stack.getCapability(ForgeCapabilities.ENERGY).map(energyStorage -> {
                        if (energyStorage.getEnergyStored() >= energyRequired) {
                            energyStorage.extractEnergy(energyRequired, false);
                            return true;
                        } else {
                            return false;
                        }
                    });


                    int newAmmo = Math.min(Math.max(0, currentAmmo + 1), maxAmmo);
                    tag.putInt("AmmoCount", newAmmo);
                    tag.putInt("RechargeCounter", refillCooldown);

                    if (entity instanceof ServerPlayer player && player.getMainHandItem().getItem() == this) { //Double check if is serverplayer and the item in the slot that was ticked is still a gun.

                        ModSyncedDataKeys.RELOADING.setValue(player, false);
                        tag.remove("IsReloading");
                        tag.remove("scguns:IsReloading");
                        tag.remove("InCriticalReloadPhase");
                        PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> {
                            return LevelLocation.create(player.level(), player.getX(), player.getY(), player.getZ(), 64.0);
                        }, new S2CMessageUpdateAmmo(tag.getInt("AmmoCount")));
                        PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> {
                            return LevelLocation.create(player.level(), player.getX(), player.getY(), player.getZ(), 64.0);
                        }, new S2CMessageReload(false));
                    }
                }
            } else {
                tag.putInt("RechargeCounter", refillCooldown);
            }
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int energyStored = getEnergyStored(stack);
        int maxEnergy = getMaxEnergyStored(stack);
        if (maxEnergy == 0) return 0;
        return Math.round(13.0F * energyStored / maxEnergy);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float ratio = (float) getEnergyStored(stack) / getMaxEnergyStored(stack);

        if (ratio > 0.66f) {
            return 0x00FFFF;
        } else if (ratio > 0.33f) {
            return 0xFFFF00;
        } else {
            return 0xFF4444;
        }
    }

    public int getEnergyStored(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    public int getMaxEnergyStored(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, worldIn, tooltip, flag);

        int energyStored = getEnergyStored(stack);
        int maxEnergy = getMaxEnergyStored(stack);
        CompoundTag tag = stack.getOrCreateTag();

        float heat_level = tag.getInt("ShotCount") / 50f * 100f;


        tooltip.add(Component.translatable("tooltip.scguns.energy").append(": ").withStyle(ChatFormatting.GRAY).append(Component.literal(String.format("%,d", energyStored)).withStyle(ChatFormatting.BLUE)).append(Component.literal(" / ").withStyle(ChatFormatting.GRAY)).append(Component.literal(String.format("%,d", maxEnergy) + " FE").withStyle(ChatFormatting.BLUE)));

        if (this.getUseFireRateRampUp()) {
            tooltip.add(Component.translatable("tooltip.scguns_cnc.heat_level").append(": ").withStyle(ChatFormatting.GRAY).append(Component.literal(String.format("%.2f", heat_level)).withStyle(ChatFormatting.RED)).append(Component.literal("%").withStyle(ChatFormatting.RED)));
        }

        tooltip.add(Component.translatable(stack.getDescriptionId() + ".lore").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));

    }
}
