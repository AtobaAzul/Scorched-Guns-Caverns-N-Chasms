package net.atobaazul.scguns_cnc.registries;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.atobaazul.scguns_cnc.common.item.BluntshotAmmoItem;
import net.atobaazul.scguns_cnc.common.item.HexRoundAmmoItem;
import net.atobaazul.scguns_cnc.common.item.MalisonGrenadeItem;
import net.atobaazul.scguns_cnc.common.item.StrikerRoundAmmoItem;
import net.atobaazul.scguns_cnc.common.item.gun.AnathemaGunItem;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.ribs.scguns.init.ModSounds;
import top.ribs.scguns.item.*;
import top.ribs.scguns.item.animated.AnimatedGunItem;

import static net.atobaazul.scguns_cnc.CompatManager.CREATE_ENABLED;
import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class ModItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);


 public static final RegistryObject<AnimatedGunItem> HANGMAN_CARBINE = REGISTER.register("hangman_carbine",
            () -> new AnimatedGunItem(
                    new Item.Properties().stacksTo(1).durability(800),
                    "hangman_carbine", // Model path
                    ModSounds.MAG_OUT.get(),        // Reload sound mag out
                    ModSounds.MAG_IN.get(),         // Reload sound mag in
                    ModSounds.RELOAD_END.get(),           // Reload sound end
                    ModSounds.COPPER_GUN_JAM.get(),      // Ejector sound pull
                    ModSounds.COPPER_GUN_JAM.get()    // Ejector sound release
            )
    );

    public static final RegistryObject<AnimatedGunItem> RIBCAGE = REGISTER.register("ribcage",
            () -> new AnimatedGunItem(
                    new Item.Properties().stacksTo(1).durability(800),
                    "ribcage", // Model path
                    ModSounds.MAG_OUT.get(),        // Reload sound mag out
                    ModSounds.MAG_IN.get(),         // Reload sound mag in
                    ModSounds.RELOAD_END.get(),           // Reload sound end
                    ModSounds.COPPER_GUN_JAM.get(),      // Ejector sound pull
                    ModSounds.COPPER_GUN_JAM.get()    // Ejector sound release
            )
    );

    public static final RegistryObject<AnimatedGunItem> BELLA = REGISTER.register("bella",
            () -> new AnimatedGunItem(
                    new Item.Properties().stacksTo(1).durability(800),
                    "bella", // Model path
                    ModSounds.MAG_OUT.get(),        // Reload sound mag out
                    ModSounds.MAG_IN.get(),         // Reload sound mag in
                    ModSounds.RELOAD_END.get(),           // Reload sound end
                    ModSounds.COPPER_GUN_JAM.get(),      // Ejector sound pull
                    ModSounds.COPPER_GUN_JAM.get()    // Ejector sound release
            )
    );

   public static final RegistryObject<AnimatedGunItem> REHEARSE = REGISTER.register("rehearse",
            () -> new AnimatedGunItem(
                    new Item.Properties().stacksTo(1).durability(800),
                    "rehearse", // Model path
                    ModSounds.MAG_OUT.get(),        // Reload sound mag out
                    ModSounds.MAG_IN.get(),         // Reload sound mag in
                    ModSounds.RELOAD_END.get(),           // Reload sound end
                    ModSounds.COPPER_GUN_JAM.get(),      // Ejector sound pull
                    ModSounds.COPPER_GUN_JAM.get()    // Ejector sound release
            )
    );

     public static final RegistryObject<AnimatedGunItem> SILVER_LINING = REGISTER.register("silverlining",
            () -> new AnimatedGunItem(
                    new Item.Properties().stacksTo(1).durability(800),
                    "silverlining", // Model path
                    ModSounds.MAG_OUT.get(),        // Reload sound mag out
                    ModSounds.MAG_IN.get(),         // Reload sound mag in
                    ModSounds.RELOAD_END.get(),           // Reload sound end
                    ModSoundEvents.SILVERLINING_PULL_MIDDLE.get(),      // Ejector sound pull
                    ModSoundEvents.SILVERLINING_PULL_END.get()    // Ejector sound release
            )
    );


    public static final RegistryObject<AnimatedGunItem> KETERIYA = REGISTER.register("keteriya",
            () -> new AnimatedGunItem(
                    new Item.Properties().stacksTo(1).durability(800),
                    "keteriya", // Model path
                    ModSounds.MAG_OUT.get(),        // Reload sound mag out
                    ModSounds.MAG_IN.get(),         // Reload sound mag in
                    ModSounds.RELOAD_END.get(),           // Reload sound end
                    ModSounds.COPPER_GUN_JAM.get(),      // Ejector sound pull
                    ModSounds.COPPER_GUN_JAM.get()    // Ejector sound release
            )
    );


    public static final RegistryObject<AnimatedGunItem> ANATHEMA = REGISTER.register("anathema",
            () -> new AnathemaGunItem(
                    new Item.Properties().stacksTo(1).durability(800),
                    "anathema", // Model path
                    ModSounds.MAG_OUT.get(),        // Reload sound mag out
                    ModSounds.MAG_IN.get(),         // Reload sound mag in
                    ModSounds.RELOAD_END.get(),           // Reload sound end
                    ModSounds.COPPER_GUN_JAM.get(),      // Ejector sound pull
                    ModSounds.COPPER_GUN_JAM.get()    // Ejector sound release
            )
    );

    public static final RegistryObject<RechargeableEnergyGunItem> LUSTRE = REGISTER.register("lustre",
            () -> new RechargeableEnergyGunItem(
                    new Item.Properties().stacksTo(1).rarity(CCItems.FANCY),
                    "lustre",
                    ModSoundEvents.LUSTRE_MAG_OUT.get(),
                    ModSoundEvents.LUSTRE_MAG_IN.get(),
                    ModSounds.RELOAD_END.get(),
                    ModSoundEvents.LUSTRE_JAM.get(),
                    ModSoundEvents.LUSTRE_JAM.get(),
                    5000,
                    40,
                    300000
            )
    );


    public static final RegistryObject<AnimatedGunItem> GALLOWS = REGISTER.register("gallows",
            () -> new AnimatedGunItem(
                    new Item.Properties().stacksTo(1).durability(800),
                    "gallows", // Model path
                    ModSounds.MAG_OUT.get(),        // Reload sound mag out
                    ModSounds.MAG_IN.get(),         // Reload sound mag in
                    ModSounds.RELOAD_END.get(),           // Reload sound end
                    ModSounds.COPPER_GUN_JAM.get(),      // Ejector sound pull
                    ModSounds.COPPER_GUN_JAM.get()    // Ejector sound release
            )
    );

    public static final RegistryObject<AnimatedGunItem> NECROSIS = REGISTER.register("necrosis",
            () -> new AnimatedGunItem(
                    new Item.Properties().stacksTo(1).durability(800),
                    "necrosis", // Model path
                    ModSounds.MAG_OUT.get(),        // Reload sound mag out
                    ModSounds.MAG_IN.get(),         // Reload sound mag in
                    ModSounds.RELOAD_END.get(),           // Reload sound end
                    ModSounds.COPPER_GUN_JAM.get(),      // Ejector sound pull
                    ModSounds.COPPER_GUN_JAM.get()    // Ejector sound release
            )
    );


    public static final RegistryObject<Item> MALISON_GRENADE = REGISTER.register("malison_grenade", () -> new MalisonGrenadeItem(new Item.Properties().stacksTo(32), 20 * 3));







    //blueprint
    public static final RegistryObject<Item> GRAVEKEEPER_BLUEPRINT = REGISTER.register("gravekeeper_blueprint", () -> new BlueprintItem(new Item.Properties().stacksTo(1)));

    //rounds
    public static final RegistryObject<Item> HEX_ROUND = REGISTER.register("hex_round", () -> new HexRoundAmmoItem(new Item.Properties()));
    public static final RegistryObject<Item> COMPACT_HEX_ROUND = REGISTER.register("compact_hex_round", () -> new HexRoundAmmoItem(new Item.Properties()));
    public static final RegistryObject<Item> BLUNTSHOT = REGISTER.register("bluntshot", () -> new BluntshotAmmoItem(new Item.Properties()));
    public static final RegistryObject<Item> STRIKER_ROUND = REGISTER.register("striker_round", () -> new StrikerRoundAmmoItem(new Item.Properties()));

    public static final RegistryObject<Item> THE_HUNGER = REGISTER.register("hunger", () -> new AmmoItem(new Item.Properties()));

    //other items
    public static final RegistryObject<Item> NECROMIUM_GUN_FRAME = REGISTER.register("necromium_gun_frame", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SMALL_NECROMIUM_CASING = REGISTER.register("small_necromium_casing", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEDIUM_NECROMIUM_CASING = REGISTER.register("medium_necromium_casing", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_BULLET = REGISTER.register("silver_bullet", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> UNFINISHED_HEX_ROUND = sequencedIngredient("unfinished_hex_round");
    public static final RegistryObject<Item> UNFINISHED_COMPACT_HEX_ROUND = sequencedIngredient("unfinished_compact_hex_round");

    public static final RegistryObject<Item> GRAVEKEEPER_FLARE = REGISTER.register("gravekeeper_flare",
            () -> new RaidFlareItem(new Item.Properties().stacksTo(16), "gravekeeper"));

    public static final RegistryObject<Item> LESSER_STRAWMAN = REGISTER.register("lesser_strawman",
            () -> new TooltipItem(new Item.Properties(),
                    "item.scguns_cnc.lesser_strawman.tooltip",
                    "item.scguns.found_in_raids"));

    private static RegistryObject<Item> sequencedIngredient(String name) {
        if (CREATE_ENABLED) {
            return REGISTER.register(name, () -> ModCompatItems.getSequencedItem(name));
        }
        return REGISTER.register(name, () -> new Item(new Item.Properties()));
    };
}