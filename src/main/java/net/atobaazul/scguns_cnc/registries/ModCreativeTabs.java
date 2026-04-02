package net.atobaazul.scguns_cnc.registries;

import com.google.common.collect.Lists;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import top.ribs.scguns.init.ModCreativeModeTabs;
import top.ribs.scguns.item.GunItem;

import java.util.ArrayList;
import java.util.function.Supplier;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.registries.ModItems.*;
import static net.minecraft.world.item.crafting.Ingredient.of;
import static top.ribs.scguns.init.ModItems.*;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);


    public static final RegistryObject<CreativeModeTab> SCGUNS_CNC_TAB = CREATIVE_MODE_TABS.register("scguns_cnc_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RIBCAGE.get())).title(Component.translatable("creativetab.scguns_cnc_tab")).displayItems((pParameters, pOutput) -> {
        addItemWithFullAmmo(pOutput, MORTICIAN.get());
        addItemWithFullAmmo(pOutput, BELLA.get());
        addItemWithFullAmmo(pOutput, REHEARSE.get());

        addItemWithFullAmmo(pOutput, ANATHEMA.get());
        addItemWithFullAmmo(pOutput, HANGMAN_CARBINE.get());
        addItemWithFullAmmo(pOutput, GALLOWS.get());
        addItemWithFullAmmo(pOutput, CACOPHONY.get());

        addItemWithFullAmmo(pOutput, KETERIYA.get());
        addItemWithFullAmmo(pOutput, NECROSIS.get());

        addItemWithFullAmmo(pOutput, SILVER_LINING.get());
        addItemWithFullAmmo(pOutput, RIBCAGE.get());

        addEnergyGunWithFullAmmo(pOutput, LUSTRE.get());
        addEnergyGunWithFullAmmo(pOutput, ELECTROTHERMAL_AUTOCANNON.get());
        addEnergyGunWithFullAmmo(pOutput, SCATTERER.get());

        addItemWithFullAmmo(pOutput, IRON_PARTISAN.get());
        addItemWithFullAmmo(pOutput, RECUR.get());
        addItemWithFullAmmo(pOutput, RASCAL.get());
        addItemWithFullAmmo(pOutput, CHARYBDIS.get());

        addItem(pOutput, MALISON_GRENADE.get());

        addItem(pOutput, HEX_ROUND.get());
        addItem(pOutput, COMPACT_HEX_ROUND.get());
        addItem(pOutput, BLUNTSHOT.get());
        addItem(pOutput, HEXSHOT.get());
        addItem(pOutput, COPPER_SLUG.get());
        addItem(pOutput, RICOSHOT_ROUND.get());
        addItem(pOutput, NECROMIUM_GUN_FRAME.get());
        addItem(pOutput, SMALL_NECROMIUM_CASING.get());
        addItem(pOutput, MEDIUM_NECROMIUM_CASING.get());
        addItem(pOutput, SILVER_BULLET.get());
        addItem(pOutput, HEX_BUCKSHOT.get());

        addItem(pOutput, GRAVEKEEPER_BLUEPRINT.get());
        addItem(pOutput, GRAVEKEEPER_FLARE.get());
        addItem(pOutput, LESSER_STRAWMAN.get());

        addItem(pOutput, VAULT_GUN_PARTS.get());
        addItem(pOutput, LUSTRE_PART.get());
        addItem(pOutput, ELECTROTHERMAL_PART.get());
        addItem(pOutput, SCATTERER_PART.get());
    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    private static void addItem(CreativeModeTab.Output output, Item item) {
        output.accept(item);
    }

    private static void addItemWithFullAmmo(CreativeModeTab.Output output, Item item) {
        ModCreativeModeTabs.CreativeTabHelper.addItemWithFullAmmo(output, item);
    }

    private static void addEnergyGunWithFullAmmo(CreativeModeTab.Output output, Item item) {
        if (item instanceof RechargeableEnergyGunItem gunItem) {
            ItemStack stack = new ItemStack(gunItem);
            stack.getOrCreateTag().putInt("Energy", gunItem.getMaxEnergyStored(stack));
            stack.getOrCreateTag().putInt("AmmoCount", gunItem.getGun().getReloads().getMaxAmmo());
            output.accept(stack);
        } else {
            output.accept(item);
        }

    }
}