package net.atobaazul.scguns_cnc.datagen.providers.client;

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
        addItem(ModItems.HEXSHOT, "Hexshot Shell");
        addItem(ModItems.HEX_BUCKSHOT, "Hex Buckshot");
        addItem(ModItems.COPPER_SLUG, "Copper Slug");
        addItem(ModItems.PULSE_CORE, "Integrated Pulse Core");
        addItem(ModItems.RICOSHOT_ROUND, "Ricoshot Round");

        //guns
        addItem(ModItems.HANGMAN_CARBINE, "Hangman Carabine");
        addItem(ModItems.RIBCAGE, "Ribcage");
        addItem(ModItems.BELLA, "Bella");
        addItem(ModItems.MORTICIAN, "Mortician");
        addItem(ModItems.REHEARSE, "Rehearse");
        addItem(ModItems.SILVER_LINING, "Silver Lining");
        addItem(ModItems.KETERIYA, "Keteriya");
        addItem(ModItems.GALLOWS, "Gallows");
        addItem(ModItems.NECROSIS, "Necrosis");
        addItem(ModItems.CACOPHONY, "Cacophony");
        addItem(ModItems.HANGMAN_ACOLYTE, "Hangman Acolyte");

        addItem(ModItems.ANATHEMA, "Anathema");

        addItem(ModItems.LUSTRE, "Lustre");
        addItem(ModItems.ELECTROTHERMAL_AUTOCANNON, "Electrothermal Autocannon");
        addItem(ModItems.SCATTERER, "Scatterer");

        addItem(ModItems.IRON_PARTISAN, "Iron Partisan");
        addItem(ModItems.RECUR, "Recur");
        addItem(ModItems.RASCAL, "Rascal");

        addItem(ModItems.MALISON_GRENADE, "Malison Grenade");
        addItem(ModItems.UNFINISHED_COMPACT_HEX_ROUND, "Unfinished Compact Hex Round");
        addItem(ModItems.UNFINISHED_HEX_ROUND, "Unfinished Hex Round");
        addItem(ModItems.UNFINISHED_HEXSHOT, "Unfinished Hexshot Shell");
        addItem(ModItems.UNFINISHED_COPPER_SLUG, "Unfinished Copper Slug");
        addItem(ModItems.UNFINISHED_RICOSHOT_ROUND, "Unfinished Ricoshot Round");
        addItem(ModItems.LESSER_STRAWMAN, "Weird Doll");
        addItem(ModItems.GRAVEKEEPER_FLARE, "Gravekeeper Flare");
        addItem(ModItems.VAULT_GUN_PARTS, "Experimental Components");
        addItem(ModItems.LUSTRE_PART, "Zirconia Laser Optics");
        addItem(ModItems.ELECTROTHERMAL_PART, "Rapid Plasma Injector");
        addItem(ModItems.SCATTERER_PART, "Plasma Accumulator");


        //tooltips
        add("tooltip.hex_round.magic_damage", "Deals half of the bullet damage as magic damage.");
        add("tooltip.bluntshot.knockback", "Deals increased knockback to hit targets.");
        add("tooltip.anathema", "Melee hits feed the \"gun\", restoring ammo.");
        add("tooltip.scguns_cnc.heat_level", "Heat Level");
        add("tooltip.ricoshot_round.crit_chance", "Increases crit chance and damage by 50% for every ricochet.");

        add("item.scguns_cnc.electrothermal_autocannon.lore", "Rapid-fire energy weapon with a welded experimental rechargeable Energy Core, originally designed for vehicles.\nOverheats during sustained fire, limiting fire rate.");
        add("item.scguns_cnc.lustre.lore", "Repurposed laser cutter with an Energy Core recharge system.\nEnergy Core can be swapped for immediate recharge.");
        add("item.scguns_cnc.scatterer.lore", "Compact electrothermal energy weapon, capable of firing single precise shots or large inaccurate bursts of plasma.");

        //tab
        add("creativetab.scguns_cnc_tab", "Scorched Guns: Caverns & Chasms");

        //desc.
        add("scguns.desc.hangman_carbine", "Compact weapon that shines in close quarters combat, being the modern standard issue firearm of the Gravekeepers.");
        add("scguns.desc.silverlining", "The oldest weapon of the Gravekeeper arsenal, with its creation dating back to the very origins of the organization. It is still widely used as a assassin's weapon by the believers of the Holy Curse.");
        add("scguns.desc.bella", " The most commonly used kinetic solution employed by the Gravekeepers while they still existed in secrecy, being compact enough to be inconspicuous and was commonly used with a silencer. ");
        add("scguns.desc.ribcage", "The Ribcage heavy rifle is wildly used by Gravekeeper marksman, firing rounds capable of harming the very essence of its targets.");
        add("scguns.desc.rehearse", "In anticipation for a time were subtlety would be no longer needed, the Gravekeepers started expanding their arsenal with larger guns. The Rehearse became the first of newly developed guns for the new world.");
        add("scguns.desc.keteriya", "Capable of launching the user and its targets, it can used as a mobility tool to close the distance and deliver devastating blow with the attached axe blade.");
        add("scguns.desc.gallows", "High-power automatic rifle, ideal for swiftly neutralizing targets. Typically used by Gravekeepers's executioner firing squads.");
        add("scguns.desc.necrosis", "Heavy combat shotgun designed to be used by Gravekeeper dungeon guards, capable of firing both Hexshot for close combat and Copper Slugs for medium range.");
        add("scguns.desc.mortician", "Standard Gravekeeper sidearm, typically given to neophytes and other lesser Gravekeepers.");
        add("scguns.desc.cacophony", "Gravekeeper light machine gun, capable of shredding even the most heavily armored enemies. The unique sound produced from its gas vent earned it its nickname.");

        add("scguns.desc.iron_partisan", "One of the first prototype guns developed to fire the, then in early in development, RicoShot rounds, mostly constructed using repurposed parts from Iron series of rifles.\nFew original units were ever made and no Partisan were ever fielded in combat before the collapse of FAC.");
        add("scguns.desc.rascal", "Compact burst-fire rifle for close-quarters engagements that don't require worrying about what and where you're hitting.");
        add("scguns.desc.recur", "A reverse-engineering of COG's \"Rascal\" rifle. Tt still keeps its utility as a chaotic close-quarters rifle.");

        add("gun_tier.scguns.gravekeeper", "Gravekeeper");
        add("raid.scguns.gravekeeper", "Gravekeeper Raid");

        //misc.
        add("scguns_cnc.jei.info.found_in_gravekeeper_structures", "Found in Gravekeeper structures.");
        add("scguns_cnc.jei.info.found_in_vaults", "Found in underground Vaults.");
        add("raid.announcement.scguns_cnc.gravekeeper", "The Gravekeepers arrived, the curse demand a sacrifice!");
        add("raid.boss.scguns_cnc.gravekeeper", "Gravekeeper Herald");
        add("item.scguns_cnc.lesser_strawman.tooltip", "A doll used in rituals. You feel it move when you're not looking.");
        add("item.scguns_cnc.vault_gun_parts.tooltip", "An array of unusually advanced electrical gizmos and doodads used as general parts for standard T.I.N. devices.");
        add("item.scguns_cnc.lustre_part.tooltip", "Technological Innovation Nucleus' advanced optics are highly sought after due to their exceptional quality, and the tools required for their fabrication no longer existing.");
        add("item.scguns_cnc.electrothermal_part.tooltip", "Technological Innovation Nucleus' plasma power project was cancelled and repurposed into military applications after growing tensions between COG and Asghar demanded more weapons of war.");
        add("item.scguns_cnc.scatterer_part.tooltip", "Technological Innovation Nucleus' plasma systems are create usable plasma from the very air, revolutionizing plasma technology.");

        add("raid.scguns.gravekeeper.name", "Summons the Gravekeeper Cult");
    }
}
