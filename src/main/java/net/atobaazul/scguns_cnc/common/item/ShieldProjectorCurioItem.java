package net.atobaazul.scguns_cnc.common.item;

import net.atobaazul.scguns_cnc.SCGunsCnC;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import top.ribs.scguns.item.exosuit.ExoSuitCoreItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class ShieldProjectorCurioItem extends Item implements ICurioItem {
    private final ForgeConfigSpec.IntValue energyRequired;
    private final ForgeConfigSpec.IntValue refillCooldown;
    private final ForgeConfigSpec.IntValue maxEnergy;
    private final ForgeConfigSpec.IntValue disintegrationCooldown;
    private final ForgeConfigSpec.DoubleValue maxHealth;
    public Double health;
    private int cooldown = 0;
    private int age = 0;

    public ShieldProjectorCurioItem(Properties pProperties, ForgeConfigSpec.IntValue energyRequired, ForgeConfigSpec.IntValue refillCooldown, ForgeConfigSpec.IntValue maxEnergy, ForgeConfigSpec.IntValue disintegrationCooldown, ForgeConfigSpec.DoubleValue maxHealth) {
        super(pProperties);
        this.energyRequired = energyRequired;
        this.refillCooldown = refillCooldown;
        this.maxEnergy = maxEnergy;
        this.disintegrationCooldown = disintegrationCooldown;
        this.maxHealth = maxHealth;
        this.health = 10.0;
        this.cooldown = 0;
    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
    }

    public Double getShieldHealth() {
        return health;
    }

    public Double getShieldMaxHealth() {
        return this.maxHealth.get();
    }

    //Energy required for shield to regen 1 health
    public int getEnergyRequired() {
        return this.energyRequired.get();
    }

    //time it takes, in ticks, to regen 1 health. Reset when taking damage.
    public int getRefillCooldown() {
        return this.refillCooldown.get();
    }

    public int getCurrentCooldown() {
        return this.cooldown;
    }

    public int getMaxEnergy() {
        return this.maxEnergy.get();
    }

    //time it takes, in ticks, for shield to regen back to full when it reaches 0 health.
    public int getDisintegrationCooldown() {
        return this.disintegrationCooldown.get();
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> new ExoSuitCoreItem.SimpleExoSuitEnergyStorage(stack, getMaxEnergy()));

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
                return cap == ForgeCapabilities.ENERGY ? energy.cast() : LazyOptional.empty();
            }
        };
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

    public void doHealthDelta(double value) {
        this.health = Mth.clamp(this.health + value, 0, this.getShieldMaxHealth());

        if (this.health <= 0) {
            this.cooldown = this.getDisintegrationCooldown();
        }
    }


    @Override
    public void curioTick(SlotContext ctx, ItemStack stack) {
        this.age++;

        if (this.age == 1) {
            initializeData(stack);
        }

        LivingEntity entity = ctx.entity();

        if (entity.level() instanceof ServerLevel level) {
            LazyOptional<IEnergyStorage> capability = stack.getCapability(ForgeCapabilities.ENERGY);
            int energyStored = capability.map(IEnergyStorage::getEnergyStored).orElse(0);

            if (cooldown > 0) {
                this.cooldown--;
            } else if (energyStored >= getEnergyRequired() && this.health < this.getShieldMaxHealth()) {
                this.doHealthDelta(1);

                stack.getCapability(ForgeCapabilities.ENERGY).map(energyStorage -> {
                    if (energyStorage.getEnergyStored() >= getEnergyRequired()) {
                        energyStorage.extractEnergy(getEnergyRequired(), false);
                        return true;
                    } else {
                        return false;
                    }
                });

                this.cooldown = this.getRefillCooldown();
            }

            //write data to nbt, so it can be synched to clients easily.
            this.deserializeData(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, worldIn, tooltip, flag);

        int energyStored = getEnergyStored(stack);
        int maxEnergy = getMaxEnergyStored(stack);

        tooltip.add(Component.translatable("tooltip.scguns.energy").append(": ").withStyle(ChatFormatting.GRAY).append(Component.literal(String.format("%,d", energyStored)).withStyle(ChatFormatting.BLUE)).append(Component.literal(" / ").withStyle(ChatFormatting.GRAY)).append(Component.literal(String.format("%,d", maxEnergy) + " FE").withStyle(ChatFormatting.BLUE)));

        tooltip.add(Component.translatable(stack.getDescriptionId() + ".lore").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
    }

    public void initializeData(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        tag.putInt("cooldown", this.cooldown);
        tag.putDouble("health", this.maxHealth.get());
    }

    public void deserializeData(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        tag.putInt("cooldown", this.cooldown);
        tag.putDouble("health", this.health);
    }

    public void serializeData(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        this.cooldown = tag.getInt("cooldown");
        this.health = Mth.clamp(tag.getDouble("health"), 0, this.getShieldMaxHealth());
    }


}
