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

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);


    public static final RegistryObject<CreativeModeTab> SCGUNS_CNC_TAB = CREATIVE_MODE_TABS.register("scguns_cnc_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RIBCAGE.get())).title(Component.translatable("creativetab.scguns_cnc_tab")).displayItems((pParameters, pOutput) -> {
        addItemWithFullAmmo(pOutput, ModItems.HANGMAN_CARBINE.get());
        addItemWithFullAmmo(pOutput, ModItems.RIBCAGE.get());
        addItemWithFullAmmo(pOutput, ModItems.BELLA.get());
        addItemWithFullAmmo(pOutput, ModItems.REHEARSE.get());
        addItemWithFullAmmo(pOutput, ModItems.SILVER_LINING.get());
        addItemWithFullAmmo(pOutput, ModItems.KETERIYA.get());
        addItemWithFullAmmo(pOutput, ModItems.ANATHEMA.get());

        addItem(pOutput, ModItems.GRAVEKEEPER_BLUEPRINT.get());
        addItem(pOutput, ModItems.HEX_ROUND.get());
        addItem(pOutput, ModItems.BLUNTSHOT.get());
        addItem(pOutput, ModItems.COMPACT_HEX_ROUND.get());
        addItem(pOutput, ModItems.NECROMIUM_GUN_FRAME.get());
        addItem(pOutput, ModItems.SMALL_NECROMIUM_CASING.get());
        addItem(pOutput, ModItems.MEDIUM_NECROMIUM_CASING.get());
        addItem(pOutput, ModItems.SILVER_BULLET.get());

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