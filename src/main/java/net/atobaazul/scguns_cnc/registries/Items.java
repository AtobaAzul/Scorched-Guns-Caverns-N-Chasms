package net.atobaazul.scguns_cnc.registries;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.ribs.scguns.Reference;
import top.ribs.scguns.init.ModSounds;
import top.ribs.scguns.item.AmmoItem;
import top.ribs.scguns.item.BlueprintItem;
import top.ribs.scguns.item.animated.AnimatedGunItem;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class Items {
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
 /*
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

     public static final RegistryObject<AnimatedGunItem> SILVER_LINING = REGISTER.register("silver_lining",
            () -> new AnimatedGunItem(
                    new Item.Properties().stacksTo(1).durability(800),
                    "silver_lining", // Model path
                    ModSounds.MAG_OUT.get(),        // Reload sound mag out
                    ModSounds.MAG_IN.get(),         // Reload sound mag in
                    ModSounds.RELOAD_END.get(),           // Reload sound end
                    ModSounds.COPPER_GUN_JAM.get(),      // Ejector sound pull
                    ModSounds.COPPER_GUN_JAM.get()    // Ejector sound release
            )
    );

  */



    //blueprint
    public static final RegistryObject<Item> GRAVEKEEPER_BLUEPRINT = REGISTER.register("gravekeeper_blueprint", () -> new BlueprintItem(new Item.Properties().stacksTo(1)));

    //arounds
    public static final RegistryObject<Item> HEX_ROUND = REGISTER.register("hex_round", () -> new AmmoItem(new Item.Properties()));
    public static final RegistryObject<Item> BLUNTSHOT = REGISTER.register("bluntshot", () -> new AmmoItem(new Item.Properties()));
    public static final RegistryObject<Item> COMPACT_HEX_ROUND = REGISTER.register("compact_hex_round", () -> new AmmoItem(new Item.Properties()));

   //other items
    public static final RegistryObject<Item> NECROMIUM_GUN_FRAME = REGISTER.register("necromium_gun_frame", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SMALL_NECROMIUM_CASING = REGISTER.register("small_necromium_casing", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEDIUM_NECROMIUM_CASING = REGISTER.register("medium_necromium_casing", () -> new Item(new Item.Properties()));

}
