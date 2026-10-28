package net.atobaazul.scguns_cnc.common.item;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.atobaazul.scguns_cnc.client.KeyBinds;
import net.atobaazul.scguns_cnc.client.render.item.BoostPackArmorRender;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ribs.scguns.item.exosuit.ExoSuitCoreItem;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static net.atobaazul.scguns_cnc.client.KeyBinds.BOOST_KEY;

public class BoostPackArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ForgeConfigSpec.IntValue energyRequired;
    private final ForgeConfigSpec.IntValue maxEnergy;
    private int age = 0;
    public boolean charged = false;

    public BoostPackArmorItem(Type pType, Properties pProperties, ForgeConfigSpec.IntValue energyRequired, ForgeConfigSpec.IntValue maxEnergy) {
        super(BoostPackMaterial.MATERIAL, pType, pProperties);
        this.energyRequired = energyRequired;
        this.maxEnergy = maxEnergy;
    }

    public static final RawAnimation ACTIVE = RawAnimation.begin().thenLoop("misc.active");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 0, state -> {
            state.setAnimation(DefaultAnimations.IDLE);

            Entity entity = state.getData(DataTickets.ENTITY);

            if (entity instanceof Player player) {
                for (ItemStack armorSlot : entity.getArmorSlots()) {
                    if (armorSlot.is(this) && this.getEnergyStored(armorSlot) > getEnergyRequired() && !player.getCooldowns().isOnCooldown(this)) {
                        state.setAnimation(ACTIVE);
                    }
                }
            }

            return PlayState.CONTINUE;
        }));
    }

    public int getEnergyRequired() {
        return this.energyRequired.get();
    }

    public int getMaxEnergy() {
        return this.maxEnergy.get();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null) this.renderer = new BoostPackArmorRender();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
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


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int pSlotId, boolean pIsSelected) {
        this.age++;

        if (this.age == 1) {
            initializeData(stack);
        }

        if (level instanceof ServerLevel sLevel && entity instanceof Player player) {
            LazyOptional<IEnergyStorage> capability = stack.getCapability(ForgeCapabilities.ENERGY);
            int energyStored = capability.map(IEnergyStorage::getEnergyStored).orElse(0);

            boolean _cooldown = player.getCooldowns().isOnCooldown(this);


           if (!_cooldown && energyStored >= getEnergyRequired() && !this.charged) {
               stack.getCapability(ForgeCapabilities.ENERGY).map(energyStorage -> {
                   if (energyStorage.getEnergyStored() >= getEnergyRequired()) {
                       energyStorage.extractEnergy(getEnergyRequired(), false);
                       return true;
                   } else {
                       return false;
                   }
               });

               this.charged = true;
           }


            //write data to nbt, so it can be synched to clients easily.
            this.deserializeData(stack);
        }
        super.inventoryTick(stack, level, entity, pSlotId, pIsSelected);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, worldIn, tooltip, flag);

        int energyStored = getEnergyStored(stack);
        int maxEnergy = getMaxEnergyStored(stack);

        tooltip.add(KeyBinds.BOOST_KEY.getKey().getDisplayName().plainCopy().withStyle(ChatFormatting.BLUE).append(Component.translatable(stack.getDescriptionId() + ".boost").withStyle(ChatFormatting.GRAY)));
        tooltip.add(Component.translatable("tooltip.scguns.energy").append(": ").withStyle(ChatFormatting.GRAY).append(Component.literal(String.format("%,d", energyStored)).withStyle(ChatFormatting.BLUE)).append(Component.literal(" / ").withStyle(ChatFormatting.GRAY)).append(Component.literal(String.format("%,d", maxEnergy) + " FE").withStyle(ChatFormatting.BLUE)));
    }

    public void initializeData(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        tag.putBoolean("charged", this.charged);
    }

    public void deserializeData(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        tag.putBoolean("charged", this.charged);
    }
}
