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








    SILVERLINGING_MAIN("silverlining/main"),

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


    HANGMAN_CARBINE_MAIN("hangman_carbine/main"),



    ANATHEMA_MAIN("anathema/main"),
    KETERIYA_MAIN("keteriya/main");


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
