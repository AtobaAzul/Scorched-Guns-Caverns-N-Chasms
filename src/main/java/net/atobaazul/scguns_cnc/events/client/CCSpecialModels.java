package net.atobaazul.scguns_cnc.events.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum CCSpecialModels {
    //Rehearse
    REHEARSE_MAIN("rehearse/main"),
    REHEARSE_DRUM("rehearse/drum"),
    REHEARSE_STAN_BARREL("rehearse/stan_barrel"),
    REHEARSE_EXT_BARREL("rehearse/ext_barrel"),
    REHEARSE_STOCK_WEIGHTED("rehearse/heavy_stock"),
    REHEARSE_STOCK_LIGHT("rehearse/light_stock"),
    REHEARSE_STOCK_WOODEN("rehearse/wooden_stock"),
    REHEARSE_STAN_GRIP("rehearse/stan_grip"),
    REHEARSE_SILENCER("rehearse/silencer"),
    REHEARSE_ADVANCED_SILENCER("rehearse/advanced_silencer"),
    REHEARSE_MUZZLE_BRAKE("rehearse/muzzle_brake"),

    //Ribcage
    RIBCAGE_MAIN("ribcage/main"),
    RIBCAGE_STAN_BARREL("ribcage/stan_barrel"),
    RIBCAGE_EXT_BARREL("ribcage/ext_barrel"),
    RIBCAGE_STOCK_WEIGHTED("ribcage/heavy_stock"),
    RIBCAGE_STOCK_LIGHT("ribcage/light_stock"),
    RIBCAGE_STOCK_WOODEN("ribcage/wooden_stock"),
    RIBCAGE_STAN_GRIP("ribcage/stan_grip"),
    RIBCAGE_SILENCER("ribcage/silencer"),
    RIBCAGE_ADVANCED_SILENCER("ribcage/advanced_silencer"),
    RIBCAGE_MUZZLE_BRAKE("ribcage/muzzle_brake"),
    RIBCAGE_NETHERITE_BAYONET("ribcage/netherite_bayonet"),
    RIBCAGE_DIAMOND_BAYONET("ribcage/diamond_bayonet"),
    RIBCAGE_ANTHRALITE_BAYONET("ribcage/anthralite_bayonet"),
    RIBCAGE_IRON_BAYONET("ribcage/iron_bayonet"),
    RIBCAGE_LIGHT_GRIP("ribcage/light_grip"),
    RIBCAGE_TACT_GRIP("ribcage/tact_grip"),

    //Necrosis
    NECROSIS_MAIN("necrosis/main"),
    NECROSIS_STAN_BARREL("necrosis/stan_barrel"),
    NECROSIS_EXT_BARREL("necrosis/ext_barrel"),
    NECROSIS_STOCK_WEIGHTED("necrosis/heavy_stock"),
    NECROSIS_STOCK_LIGHT("necrosis/light_stock"),
    NECROSIS_STOCK_WOODEN("necrosis/wooden_stock"),
    NECROSIS_STAN_GRIP("necrosis/stan_grip"),
    NECROSIS_SILENCER("necrosis/silencer"),
    NECROSIS_ADVANCED_SILENCER("necrosis/advanced_silencer"),
    NECROSIS_MUZZLE_BRAKE("necrosis/muzzle_brake"),
    NECROSIS_NETHERITE_BAYONET("necrosis/netherite_bayonet"),
    NECROSIS_DIAMOND_BAYONET("necrosis/diamond_bayonet"),
    NECROSIS_ANTHRALITE_BAYONET("necrosis/anthralite_bayonet"),
    NECROSIS_IRON_BAYONET("necrosis/iron_bayonet"),
    NECROSIS_LIGHT_GRIP("necrosis/light_grip"),
    NECROSIS_TACT_GRIP("necrosis/tact_grip"),

    //Silver lining
    SILVERLINGING_MAIN("silverlining/main"),
    SILVERLINGING_STAN_MAGAZINE("silverlining/stand_mag"),
    SILVERLINGING_FAST_MAGAZINE("silverlining/fast_mag"),
    SILVERLINGING_EXT_MAGAZINE("silverlining/ext_mag"),
    SILVERLINGING_STOCK_WEIGHTED("silverlining/heavy_stock"),
    SILVERLINGING_STOCK_LIGHT("silverlining/light_stock"),
    SILVERLINGING_STOCK_WOODEN("silverlining/wooden_stock"),
    SILVERLINGING_NETHERITE_BAYONET("silverlining/netherite_bayonet"),
    SILVERLINGING_DIAMOND_BAYONET("silverlining/diamond_bayonet"),
    SILVERLINGING_ANTHRALITE_BAYONET("silverlining/anthralite_bayonet"),
    SILVERLINGING_IRON_BAYONET("silverlining/iron_bayonet"),
    SILVERLINGING_LIGHT_GRIP("silverlining/light_grip"),
    SILVERLINGING_TACT_GRIP("silverlining/tact_grip"),

    //Bella
    BELLA_MAIN("bella/main"),
    BELLA_STAN_MAGAZINE("bella/stan_magazine"),
    BELLA_FAST_MAGAZINE("bella/fast_magazine"),
    BELLA_EXT_MAGAZINE("bella/ext_magazine"),
    BELLA_STAN_BARREL("bella/stan_barrel"),
    BELLA_EXT_BARREL("bella/ext_barrel"),
    BELLA_STOCK_WEIGHTED("bella/heavy_stock"),
    BELLA_STOCK_LIGHT("bella/light_stock"),
    BELLA_STOCK_WOODEN("bella/wooden_stock"),
    BELLA_STAN_GRIP("bella/stan_grip"),
    BELLA_SILENCER("bella/silencer"),
    BELLA_ADVANCED_SILENCER("bella/advanced_silencer"),
    BELLA_MUZZLE_BRAKE("bella/muzzle_brake"),

    //hangman
    HANGMAN_CARBINE_MAIN("hangman_carbine/main"),
    HANGMAN_STAN_MAGAZINE("hangman_carbine/stand_mag"),
    HANGMAN_FAST_MAGAZINE("hangman_carbine/fast_mag"),
    HANGMAN_EXT_MAGAZINE("hangman_carbine/ext_mag"),
    HANGMAN_STAN_BARREL("hangman_carbine/stand_barrel"),
    HANGMAN_EXT_BARREL("hangman_carbine/ext_barrel"),
    HANGMAN_STOCK_WEIGHTED("hangman_carbine/heavy_stock"),
    HANGMAN_STOCK_LIGHT("hangman_carbine/light_stock"),
    HANGMAN_STOCK_WOODEN("hangman_carbine/wooden_stock"),
    HANGMAN_STAN_GRIP("hangman_carbine/stand_grip"),
    HANGMAN_SILENCER("hangman_carbine/silencer"),
    HANGMAN_ADVANCED_SILENCER("hangman_carbine/advanced_silencer"),
    HANGMAN_MUZZLE_BRAKE("hangman_carbine/muzzle_brake"),
    HANGMAN_NETHERITE_BAYONET("hangman_carbine/netherite_bayonet"),
    HANGMAN_DIAMOND_BAYONET("hangman_carbine/diamond_bayonet"),
    HANGMAN_ANTHRALITE_BAYONET("hangman_carbine/the_voices"),
    HANGMAN_IRON_BAYONET("hangman_carbine/iron_bayonet"),
    HANGMAN_LIGHT_GRIP("hangman_carbine/light_grip"),
    HANGMAN_TACT_GRIP("hangman_carbine/tact_grip"),

    //Gallows
    GALLOWS_MAIN("gallows/main"),
    GALLOWS_STAN_MAGAZINE("gallows/stand_mag"),
    GALLOWS_FAST_MAGAZINE("gallows/fast_mag"),
    GALLOWS_EXT_MAGAZINE("gallows/ext_mag"),
    GALLOWS_STAN_BARREL("gallows/stand_barrel"),
    GALLOWS_EXT_BARREL("gallows/ext_barrel"),
    GALLOWS_STOCK_WEIGHTED("gallows/heavy_stock"),
    GALLOWS_STOCK_LIGHT("gallows/light_stock"),
    GALLOWS_STOCK_WOODEN("gallows/wooden_stock"),
    GALLOWS_STAN_GRIP("gallows/stand_grip"),
    GALLOWS_SILENCER("gallows/silencer"),
    GALLOWS_ADVANCED_SILENCER("gallows/advanced_silencer"),
    GALLOWS_MUZZLE_BRAKE("gallows/muzzle_brake"),
    GALLOWS_NETHERITE_BAYONET("gallows/netherite_bayonet"),
    GALLOWS_DIAMOND_BAYONET("gallows/diamond_bayonet"),
    GALLOWS_ANTHRALITE_BAYONET("gallows/the_voices"),
    GALLOWS_IRON_BAYONET("gallows/iron_bayonet"),
    GALLOWS_LIGHT_GRIP("gallows/light_grip"),
    GALLOWS_TACT_GRIP("gallows/tact_grip"),

    //Mortician

    MORTICIAN_MAIN("mortician/main"),
    MORTICIAN_STAN_MAGAZINE("mortician/stan_magazine"),
    MORTICIAN_FAST_MAGAZINE("mortician/fast_magazine"),
    MORTICIAN_EXT_MAGAZINE("mortician/ext_magazine"),
    MORTICIAN_STAN_BARREL("mortician/stan_barrel"),
    MORTICIAN_EXT_BARREL("mortician/ext_barrel"),
    MORTICIAN_SILENCER("mortician/silencer"),
    MORTICIAN_ADVANCED_SILENCER("mortician/advanced_silencer"),
    MORTICIAN_MUZZLE_BRAKE("mortician/muzzle_brake"),

    CACOPHONY_MAIN("cacophony/main"),
    CACOPHONY_STAN_MAGAZINE("cacophony/stand_mag"),
    CACOPHONY_FAST_MAGAZINE("cacophony/fast_mag"),
    CACOPHONY_EXT_MAGAZINE("cacophony/ext_mag"),
    CACOPHONY_STAN_BARREL("cacophony/stand_barrel"),
    CACOPHONY_EXT_BARREL("cacophony/ext_barrel"),
    CACOPHONY_STOCK_WEIGHTED("cacophony/heavy_stock"),
    CACOPHONY_STOCK_LIGHT("cacophony/light_stock"),
    CACOPHONY_STOCK_WOODEN("cacophony/wooden_stock"),
    CACOPHONY_STAN_GRIP("cacophony/stand_grip"),
    CACOPHONY_SILENCER("cacophony"),
    CACOPHONY_ADVANCED_SILENCER("cacophony/advanced_silencer"),
    CACOPHONY_MUZZLE_BRAKE("cacophony/muzzle_brake"),
    CACOPHONY_NETHERITE_BAYONET("cacophony/netherite_bayonet"),
    CACOPHONY_DIAMOND_BAYONET("cacophony/diamond_bayonet"),
    CACOPHONY_ANTHRALITE_BAYONET("cacophony/the_voices"),
    CACOPHONY_IRON_BAYONET("cacophony/iron_bayonet"),
    CACOPHONY_LIGHT_GRIP("cacophony/light_grip"),
    CACOPHONY_TACT_GRIP("cacophony/tact_grip"),


    //Charybdis,
    CHARYBDIS_MAIN("charybdis/main"),
    CHARYBDIS_STAN_BARREL("charybdis/stan_barrel"),
    CHARYBDIS_EXT_BARREL("charybdis/ext_barrel"),
    CHARYBDIS_SILENCER("charybdis/silencer"),
    CHARYBDIS_ADVANCED_SILENCER("charybdis/advanced_silencer"),
    CHARYBDIS_MUZZLE_BRAKE("charybdis/muzzle_brake"),


    //Lustre
    LUSTRE_MAIN("lustre/main"),

    //Anathema
    ANATHEMA_MAIN("anathema/main"),

    //Keteriya
    KETERIYA_MAIN("keteriya/main"),

     //Electrothermal Autocannon
     EAUTOCANNON_MAIN("electrothermal_autocannon/main"),

    //Scatterer
    SCATTERER_MAIN("scatterer/main"),

    //RASCAL
    RASCAL_MAIN("rascal/main"),
    RASCAL_STAN_MAGAZINE("rascal/stand_mag"),
    RASCAL_FAST_MAGAZINE("rascal/fast_mag"),
    RASCAL_EXT_MAGAZINE("rascal/ext_mag"),
    RASCAL_STAN_BARREL("rascal/stand_barrel"),
    RASCAL_EXT_BARREL("rascal/ext_barrel"),
    RASCAL_STOCK_WEIGHTED("rascal/heavy_stock"),
    RASCAL_STOCK_LIGHT("rascal/light_stock"),
    RASCAL_STOCK_WOODEN("rascal/wooden_stock"),
    RASCAL_STAN_GRIP("rascal/stand_grip"),
    RASCAL_SILENCER("rascal/silencer"),
    RASCAL_ADVANCED_SILENCER("rascal/advanced_silencer"),
    RASCAL_MUZZLE_BRAKE("rascal/muzzle_brake"),
    //RECUR
    RECUR_MAIN("recur/main"),
    RECUR_STAN_MAGAZINE("recur/stand_mag"),
    RECUR_FAST_MAGAZINE("recur/fast_mag"),
    RECUR_EXT_MAGAZINE("recur/ext_mag"),
    RECUR_STAN_BARREL("recur/stand_barrel"),
    RECUR_EXT_BARREL("recur/ext_barrel"),
    RECUR_STOCK_WEIGHTED("recur/heavy_stock"),
    RECUR_STOCK_LIGHT("recur/light_stock"),
    RECUR_STOCK_WOODEN("recur/wooden_stock"),
    RECUR_STAN_GRIP("recur/stand_grip"),
    RECUR_SILENCER("recur/silencer"),
    RECUR_ADVANCED_SILENCER("recur/advanced_silencer"),
    RECUR_MUZZLE_BRAKE("recur/muzzle_brake"),


    //RECUR
    IRON_PARTISAN_MAIN("iron_partisan/main"),
    IRON_PARTISAN_STAN_MAGAZINE("iron_partisan/stand_mag"),
    IRON_PARTISAN_FAST_MAGAZINE("iron_partisan/fast_mag"),
    IRON_PARTISAN_EXT_MAGAZINE("iron_partisan/ext_mag"),
    IRON_PARTISAN_STAN_BARREL("iron_partisan/stand_barrel"),
    IRON_PARTISAN_EXT_BARREL("iron_partisan/ext_barrel"),
    IRON_PARTISAN_STOCK_WEIGHTED("iron_partisan/heavy_stock"),
    IRON_PARTISAN_STOCK_LIGHT("iron_partisan/light_stock"),
    IRON_PARTISAN_STOCK_WOODEN("iron_partisan/wooden_stock"),
    IRON_PARTISAN_STAN_GRIP("iron_partisan/stand_grip"),
    IRON_PARTISAN_SILENCER("iron_partisan/silencer"),
    IRON_PARTISAN_ADVANCED_SILENCER("iron_partisan/advanced_silencer"),
    IRON_PARTISAN_MUZZLE_BRAKE("iron_partisan/muzzle_brake");



    /**
     * The location of an item model in the [MOD_ID]/models/special/[NAME] folder
     */
    private final ResourceLocation modelLocation;

    /**
     * Cached model
     */
    private BakedModel cachedModel;

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    CCSpecialModels(String modelName) {
        this.modelLocation = new ResourceLocation(MOD_ID, "special/" + modelName);
    }

    /**
     * Registers the special models into the Forge Model Bakery. This is only called once on the
     * load of the game.
     */
    @SubscribeEvent
    public static void registerAdditional(ModelEvent.RegisterAdditional event) {
        for (CCSpecialModels model : values()) {
            event.register(model.modelLocation);
        }
    }

    /**
     * Clears the cached BakedModel since it's been rebuilt. This is needed since the models may
     * have changed when a resource pack was applied, or if resources are reloaded.
     */
    @SubscribeEvent
    public static void onBake(ModelEvent.BakingCompleted event) {
        for (CCSpecialModels model : values()) {
            model.cachedModel = null;
        }
    }

    /**
     * Gets the model
     *
     * @return isolated model
     */
    public BakedModel getModel() {
        if (this.cachedModel == null) {
            this.cachedModel = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
        }
        return this.cachedModel;
    }
}
