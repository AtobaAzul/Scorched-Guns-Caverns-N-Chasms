package net.atobaazul.scguns_cnc.client;

import net.atobaazul.scguns_cnc.client.render.gun.model.AnathemaModel;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.ribs.scguns.client.render.gun.ModelOverrides;

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
        ModelOverrides.register(ModItems.ANATHEMA.get(), new AnathemaModel());
    }
}

