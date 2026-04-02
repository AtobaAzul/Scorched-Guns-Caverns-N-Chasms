package net.atobaazul.scguns_cnc.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.energy.IEnergyStorage;

@AutoRegisterCapability
public class GunEnergyStorageCapability implements IEnergyStorage {
    private final ItemStack stack;
    private final int capacity;

    public GunEnergyStorageCapability(ItemStack stack, int capacity) {
        this.stack = stack;
        this.capacity = capacity;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int currentEnergy = getEnergyFromNBT();
        int energyReceived = Math.min(capacity - currentEnergy, maxReceive);

        if (!simulate && energyReceived > 0) {
            setEnergyToNBT(currentEnergy + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int currentEnergy = getEnergyFromNBT();
        int energyExtracted = Math.min(currentEnergy, maxExtract);

        if (!simulate && energyExtracted > 0) {
            setEnergyToNBT(currentEnergy - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return getEnergyFromNBT();
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    private int getEnergyFromNBT() {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("Energy", Tag.TAG_INT) ? tag.getInt("Energy") : 0;
    }

    private void setEnergyToNBT(int energy) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Energy", Math.max(0, Math.min(energy, capacity)));
    }

}
