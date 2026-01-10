package top.ribs.scguns.common.item.gun;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import top.ribs.scguns.item.animated.AnimatedGunItem;
import top.ribs.scguns.item.exosuit.ExoSuitCoreItem;

import javax.annotation.Nullable;
import java.util.List;

//You might be asking yourself "why is this file not under the net.atobaazul.scguns_cnc package?!"
//Well, that's because of several things regarding reload and casing ejection packets EXPLICITLY requiring that the
//gun item's class be under the 'top.ribs.scguns' packages! FOR SOME REASON!!!

public class RechargeableEnergyGunItem extends AnimatedGunItem implements GeoAnimatable, GeoItem {
    static int energyRequired = 7500;
    int counter = 0;

    public RechargeableEnergyGunItem(Properties properties, String path, SoundEvent reloadSoundMagOut, SoundEvent reloadSoundMagIn, SoundEvent reloadSoundEnd, SoundEvent boltPullSound, SoundEvent boltReleaseSound) {
        super(properties, path, reloadSoundMagOut, reloadSoundMagIn, reloadSoundEnd, boltPullSound, boltReleaseSound);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> new ExoSuitCoreItem.SimpleExoSuitEnergyStorage(stack, 100000));

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
                return cap == ForgeCapabilities.ENERGY ? energy.cast() : LazyOptional.empty();
            }
        };
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        counter++;

        CompoundTag tag = stack.getOrCreateTag();
        int currentAmmo = tag.getInt("AmmoCount");

        LazyOptional<IEnergyStorage> capabilitiy = stack.getCapability(ForgeCapabilities.ENERGY);

        int energyStored = capabilitiy.map(IEnergyStorage::getEnergyStored).orElse(0);
        if (energyStored >= energyRequired && currentAmmo < 30) {
            if (counter > 40) {
                stack.getCapability(ForgeCapabilities.ENERGY).map(energyStorage -> {
                    if (energyStorage.getEnergyStored() >= energyRequired) {
                        energyStorage.extractEnergy(energyRequired, false);
                        return true;
                    } else {
                        return false;
                    }
                });

                int newAmmo = Math.min(Math.max(0, currentAmmo + 1), 30);
                tag.putInt("AmmoCount", newAmmo);

                counter = 0;
            }
        } else {
            counter = 0;
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

        tooltip.add(Component.translatable("tooltip.scguns.energy").append(": ").withStyle(ChatFormatting.GRAY).append(Component.literal(String.format("%,d", energyStored)).withStyle(ChatFormatting.BLUE)).append(Component.literal(" / ").withStyle(ChatFormatting.GRAY)).append(Component.literal(String.format("%,d", maxEnergy) + " FE").withStyle(ChatFormatting.BLUE)));
    }
}
