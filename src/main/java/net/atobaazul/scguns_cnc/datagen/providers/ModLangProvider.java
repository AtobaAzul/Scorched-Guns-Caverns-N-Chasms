package net.atobaazul.scguns_cnc.datagen.providers;

import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        //items
        addItem(ModItems.BLUNTSHOT, "Bluntshot");
        addItem(ModItems.GRAVEKEEPER_BLUEPRINT, "Gravekeeper Blueprint");
        addItem(ModItems.COMPACT_HEX_ROUND, "Compact Hex Round");
        addItem(ModItems.HEX_ROUND, "Hex Round");
        addItem(ModItems.MEDIUM_NECROMIUM_CASING, "Necromium Casing");
        addItem(ModItems.SMALL_NECROMIUM_CASING, "Small Necromium Casing");
        addItem(ModItems.NECROMIUM_GUN_FRAME, "Necromium Gun Frame");
        addItem(ModItems.SILVER_BULLET, "Necromium Gun Frame");
        addItem(ModItems.THE_HUNGER, "The Hunger");

        //guns
        addItem(ModItems.HANGMAN_CARBINE, "Hangman Carabine");
        addItem(ModItems.RIBCAGE, "Ribcage");
        addItem(ModItems.BELLA, "Bella");
        addItem(ModItems.REHEARSE, "Rehearse");
        addItem(ModItems.SILVER_LINING, "Silver Lining");
        addItem(ModItems.KETERIYA, "Keteriya");
        addItem(ModItems.ANATHEMA, "Anathema");

        add("creativetab.scguns_cnc_tab", "Scorched Guns: Caverns & Chasms");
    }
}
