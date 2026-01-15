package net.atobaazul.scguns_cnc.client;

import net.atobaazul.scguns_cnc.client.render.gun.model.*;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.ribs.scguns.client.render.gun.ModelOverrides;
import top.ribs.scguns.client.render.gun.model.RailworkerModel;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class CCClientHandler {
    public static void registerClientHandlers(IEventBus bus) {
        bus.addListener(CCClientHandler::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(CCClientHandler::setup);
    }

    public static void setup() {
        registerModelOverrides();
    }


    private static void registerModelOverrides() {
        ModelOverrides.register(ModItems.HANGMAN_CARBINE.get(), new HangmanCarbineModel());
        ModelOverrides.register(ModItems.KETERIYA.get(), new KeteriyaModel());
        ModelOverrides.register(ModItems.ANATHEMA.get(), new AnathemaModel());
        ModelOverrides.register(ModItems.BELLA.get(), new BellaModel());
        ModelOverrides.register(ModItems.SILVER_LINING.get(), new SilverliningModel());
        ModelOverrides.register(ModItems.RIBCAGE.get(), new RibcageModel());
        ModelOverrides.register(ModItems.REHEARSE.get(), new RehearseModel());
        ModelOverrides.register(ModItems.LUSTRE.get(), new LustreModel());
        ModelOverrides.register(ModItems.GALLOWS.get(), new GallowsModel());

    }}

