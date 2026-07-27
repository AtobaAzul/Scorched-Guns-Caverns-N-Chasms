package net.atobaazul.scguns_cnc.client;

import net.atobaazul.scguns_cnc.client.render.entity.model.*;
import net.atobaazul.scguns_cnc.client.render.gun.model.*;
import net.atobaazul.scguns_cnc.registries.ModEntities;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
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
        ModelOverrides.register(ModItems.HANGMAN_CARBINE.get(), new HangmanCarbineModel());
        ModelOverrides.register(ModItems.KETERIYA.get(), new KeteriyaModel());
        ModelOverrides.register(ModItems.ANATHEMA.get(), new AnathemaModel());
        ModelOverrides.register(ModItems.BELLA.get(), new BellaModel());
        ModelOverrides.register(ModItems.SILVER_LINING.get(), new SilverliningModel());
        ModelOverrides.register(ModItems.RIBCAGE.get(), new RibcageModel());
        ModelOverrides.register(ModItems.REHEARSE.get(), new RehearseModel());
        ModelOverrides.register(ModItems.LUSTRE.get(), new LustreModel());
        ModelOverrides.register(ModItems.GALLOWS.get(), new GallowsModel());
        ModelOverrides.register(ModItems.NECROSIS.get(), new NecrosisModel());
        ModelOverrides.register(ModItems.MORTICIAN.get(), new MorticianModel());
        ModelOverrides.register(ModItems.CACOPHONY.get(), new CacophonyModel());
        ModelOverrides.register(ModItems.ELECTROTHERMAL_AUTOCANNON.get(), new EAutocannonModel());
        ModelOverrides.register(ModItems.SCATTERER.get(), new ScattererModel());
        ModelOverrides.register(ModItems.HANGMAN_ACOLYTE.get(), new HangmanAcolyteModel());
        ModelOverrides.register(ModItems.MORTICIAN_ACOLYTE.get(), new MorticianAcolyteModel());
        ModelOverrides.register(ModItems.SCHISMATIC_GALLOWS.get(), new SchismaticGallowsModel());
        ModelOverrides.register(ModItems.SCHISMATIC_REHEARSE.get(), new SchismaticRehearseModel());
        ModelOverrides.register(ModItems.EXHUMER.get(), new ExhumerModel());
        ModelOverrides.register(ModItems.DEAD_DRAFT.get(), new DeadDraftModel());

        ModelOverrides.register(ModItems.CHARYBDIS.get(), new CharybdisModel());
        ModelOverrides.register(ModItems.RASCAL.get(), new RascalModel());
        ModelOverrides.register(ModItems.RECUR.get(), new RecurModel());
        ModelOverrides.register(ModItems.IRON_PARTISAN.get(), new IronPartisanModel());
        ModelOverrides.register(ModItems.HUMMER.get(), new HummerModel());
        //ModelOverrides.register(ModItems.FUSILLADE.get(), new FusilladeModel());

        EntityRenderers.register(ModEntities.GRAVEKEEPER_GHOUL.get(), GravekeeperGhoulEntityRenderer::new);
        EntityRenderers.register(ModEntities.GRAVEKEEPER_NEOPHYTE.get(), GravekeeperNeophyteEntityRenderer::new);
        EntityRenderers.register(ModEntities.GRAVEKEEPER_HERALD.get(), GravekeeperHeraldEntityRenderer::new);
        EntityRenderers.register(ModEntities.GRAVEKEEPER_ACOLYTE.get(), GravekeeperAcolyteEntityRenderer::new);
        EntityRenderers.register(ModEntities.GRAVEKEEPER_SCHISM.get(), GravekeeperSchismEntityRenderer::new);


    }
}

