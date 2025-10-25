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
        addItem(ModItems.SILVER_BULLET, "Silver Bullet");
        addItem(ModItems.THE_HUNGER, "The Hunger");

        //guns
        addItem(ModItems.HANGMAN_CARBINE, "Hangman Carabine");
        addItem(ModItems.RIBCAGE, "Ribcage");
        addItem(ModItems.BELLA, "Bella");
        addItem(ModItems.REHEARSE, "Rehearse");
        addItem(ModItems.SILVER_LINING, "Silver Lining");
        addItem(ModItems.KETERIYA, "Keteriya");
        addItem(ModItems.ANATHEMA, "Anathema");

        addItem(ModItems.UNFINISHED_COMPACT_HEX_ROUND, "Unfinished Compact Hex Round");
        addItem(ModItems.UNFINISHED_HEX_ROUND, "Unfinished Hex Round");
        addItem(ModItems.LESSER_STRAWMAN, "Wierd Doll");
        addItem(ModItems.GRAVEKEEPER_FLARE, "Gravekeeper Flare");

        //tooltips
        add("tooltip.hex_round.magic_damage", "Deals half of the bullet damage as magic damage.");
        add("tooltip.bluntshot.knockback", "Deals increased knockback to hit targets.");
        add("tooltip.anathema", "Melee hits feed the \"gun\", restoring ammo.");

        //tab
        add("creativetab.scguns_cnc_tab", "Scorched Guns: Caverns & Chasms");

        //desc.
        add("scguns.desc.hangman_carbine", "Compact weapon that shines in close quarters combat, being the modern standard issue firearm of the Gravekeepers.");
        add("scguns.desc.silverlining", "The oldest weapon of the Gravekeeper arsenal, with its creation dating back to the very origins of the organization. It is still widely used as a assassin's weapon by the believers of the Holy Curse.");
        add("scguns.desc.bella", " The most commonly used kinetic solution employed by the Gravekeepers while they still existed in secrecy, being compact enough to be inconspicuous and was commonly used with a silencer. ");
        add("scguns.desc.ribcage", "The Ribcage heavy rifle is wildly used by Gravekeeper marksman, firing rounds capable of harming the very essence of its targets.");
        add("scguns.desc.rehearse", "In anticipation for a time were subtlety would be no longer needed, the Gravekeepers started expanding their arsenal with larger guns. The Rehearse became the first of newly developed guns for the new world.");
        add("scguns.desc.keteriya", "Capable of launching the user and its targets, it can used as a mobility tool to close the distance and deliver devastating blow with the attached axe blade.");

        add("gun_tier.scguns.gravekeeper", "Gravekeeper");
        add("raid.scguns.gravekeeper", "Gravekeeper Raid");

        //misc.
        add("scguns_cnc.jei.info.gravekeeper_blueprint", "Can be obtained from Graveyard structures.");
        add("raid.announcement.scguns_cnc.gravekeeper", "The Gravekeepers arrived, the curse demand a sacrifice!");
        add("raid.boss.scguns_cnc.gravekeeper", "Gravekeeper Herald");
        add("item.scguns_cnc.lesser_strawman.tooltip", "A doll used in rituals. You feel it move when you're not looking.");
        add("raid.scguns.gravekeeper.name", "Summons the Gravekeeper Cult");
    }
}
