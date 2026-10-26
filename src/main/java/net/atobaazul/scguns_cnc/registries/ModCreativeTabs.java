package net.atobaazul.scguns_cnc.registries;

import com.google.common.collect.Lists;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import net.atobaazul.scguns_cnc.common.item.BoostPackArmorItem;
import net.atobaazul.scguns_cnc.common.item.MalisonGrenadeItem;
import net.atobaazul.scguns_cnc.common.item.PlasmaBursterItem;
import net.atobaazul.scguns_cnc.common.item.ShieldProjectorCurioItem;
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


    public static final RegistryObject<CreativeModeTab> SCGUNS_CNC_TAB = CREATIVE_MODE_TABS.register("scguns_cnc_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RIBCAGE.get())).title(Component.translatable("creativetab.scguns_cnc_tab")).displayItems((pParameters, out) -> {
        addItemWithFullAmmo(out, MORTICIAN.get());
        addItemWithFullAmmo(out, MORTICIAN_ACOLYTE.get());
        addItemWithFullAmmo(out, BELLA.get());
        addItemWithFullAmmo(out, REHEARSE.get());
        addItemWithFullAmmo(out, SCHISMATIC_REHEARSE.get());

        addItemWithFullAmmo(out, ANATHEMA.get());
        addItemWithFullAmmo(out, HANGMAN_CARBINE.get());
        addItemWithFullAmmo(out, HANGMAN_ACOLYTE.get());

        addItemWithFullAmmo(out, GALLOWS.get());
        addItemWithFullAmmo(out, SCHISMATIC_GALLOWS.get());
        addItemWithFullAmmo(out, CACOPHONY.get());

        addItemWithFullAmmo(out, KETERIYA.get());
        addItemWithFullAmmo(out, NECROSIS.get());

        addItemWithFullAmmo(out, SILVER_LINING.get());
        addItemWithFullAmmo(out, RIBCAGE.get());
        addItemWithFullAmmo(out, DEAD_DRAFT.get());

        addItemWithFullAmmo(out, EXHUMER.get());


        addEnergyGunWithFullAmmo(out, LUSTRE.get());
        addEnergyGunWithFullAmmo(out, ELECTROTHERMAL_AUTOCANNON.get());
        addEnergyGunWithFullAmmo(out, SCATTERER.get());
        //addEnergyGunWithFullAmmo(out, STRIX_RL.get());

        addItemWithFullAmmo(out, HUMMER.get());
        addItemWithFullAmmo(out, IRON_PARTISAN.get());
        addItemWithFullAmmo(out, FUSILLADE.get());
        addItemWithFullAmmo(out, RECUR.get());
        addItemWithFullAmmo(out, RASCAL.get());
        addItemWithFullAmmo(out, CHARYBDIS.get());

        addItem(out, MALISON_GRENADE.get());
        addItem(out, PLASMA_BURSTER.get());
        addItem(out, DEPLETED_PLASMA_BURSTER.get());
        addShieldItem(out, HARDLIGHT_SHIELD_PROJECTOR.get());
        addBoostPack(out, BOOST_PACK.get());

        addItem(out, HEX_ROUND.get());
        addItem(out, COMPACT_HEX_ROUND.get());
        addItem(out, BLUNTSHOT.get());
        addItem(out, HEXSHOT.get());
        addItem(out, COPPER_SLUG.get());
        addItem(out, RICOSHOT_ROUND.get());
        addItem(out, NECROMIUM_GUN_FRAME.get());
        addItem(out, SMALL_NECROMIUM_CASING.get());
        addItem(out, MEDIUM_NECROMIUM_CASING.get());
        addItem(out, SILVER_BULLET.get());
        addItem(out, HEX_BUCKSHOT.get());

        addItem(out, GRAVEKEEPER_BLUEPRINT.get());
        addItem(out, GRAVEKEEPER_FLARE.get());
        addItem(out, LESSER_STRAWMAN.get());

        addItem(out, VAULT_GUN_PARTS.get());
        addItem(out, LUSTRE_PART.get());
        addItem(out, ELECTROTHERMAL_PART.get());
        addItem(out, SCATTERER_PART.get());

        addItem(out, GRAVEKEEPER_GHOUL_SPAWN_EGG.get());
        addItem(out, GRAVEKEEPER_NEOPHYTE_SPAWN_EGG.get());
        addItem(out, GRAVEKEEPER_HERALD_SPAWN_EGG.get());
        addItem(out, GRAVEKEEPER_ACOLYTE_SPAWN_EGG.get());
        addItem(out, GRAVEKEEPER_SCHISM_SPAWN_EGG.get());

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

    private static void addShieldItem(CreativeModeTab.Output output, Item item) {
        if (item instanceof ShieldProjectorCurioItem shield) {
            ItemStack stack = new ItemStack(shield);
            stack.getOrCreateTag().putInt("Energy", shield.getMaxEnergyStored(stack));
            output.accept(stack);
        } else {
            output.accept(item);
        }
    }

    private static void addBoostPack(CreativeModeTab.Output output, Item item) {
        if (item instanceof BoostPackArmorItem shield) {
            ItemStack stack = new ItemStack(shield);
            stack.getOrCreateTag().putInt("Energy", shield.getMaxEnergyStored(stack));
            output.accept(stack);
        } else {
            output.accept(item);
        }
    }
}