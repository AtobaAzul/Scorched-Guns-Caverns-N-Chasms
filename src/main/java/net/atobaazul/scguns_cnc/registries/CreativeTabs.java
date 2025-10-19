package net.atobaazul.scguns_cnc.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import top.ribs.scguns.init.ModCreativeModeTabs;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class CreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);


    public static final RegistryObject<CreativeModeTab> SCGUNS_CNC_TAB = CREATIVE_MODE_TABS.register("scguns_cnc_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.RIBCAGE.get())).title(Component.translatable("creativetab.scguns_cnc_tab")).displayItems((pParameters, pOutput) -> {
        addItemWithFullAmmo(pOutput, Items.HANGMAN_CARBINE.get());
        addItemWithFullAmmo(pOutput, Items.RIBCAGE.get());
        addItemWithFullAmmo(pOutput, Items.BELLA.get());
        addItemWithFullAmmo(pOutput, Items.REHEARSE.get());
        addItemWithFullAmmo(pOutput, Items.SILVER_LINING.get());
        addItemWithFullAmmo(pOutput, Items.KETERIYA.get());
        addItemWithFullAmmo(pOutput, Items.ANATHEMA.get());

        addItem(pOutput, Items.GRAVEKEEPER_BLUEPRINT.get());
        addItem(pOutput, Items.HEX_ROUND.get());
        addItem(pOutput, Items.BLUNTSHOT.get());
        addItem(pOutput, Items.COMPACT_HEX_ROUND.get());
        addItem(pOutput, Items.NECROMIUM_GUN_FRAME.get());
        addItem(pOutput, Items.SMALL_NECROMIUM_CASING.get());
        addItem(pOutput, Items.MEDIUM_NECROMIUM_CASING.get());
        addItem(pOutput, Items.SILVER_BULLET.get());

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
}