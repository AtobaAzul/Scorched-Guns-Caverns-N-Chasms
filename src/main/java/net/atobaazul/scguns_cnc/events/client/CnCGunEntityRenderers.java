package net.atobaazul.scguns_cnc.events.client;

import net.atobaazul.scguns_cnc.registries.Entities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.ribs.scguns.client.render.entity.ProjectileRenderer;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;


@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CnCGunEntityRenderers {

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event)
    {

        event.registerEntityRenderer(Entities.HEX_ROUND_PROJECTILE.get(), ProjectileRenderer::new);
        event.registerEntityRenderer(Entities.BLUNTSHOT.get(), ProjectileRenderer::new);
        event.registerEntityRenderer(Entities.DUMMY_PROJECTILE.get(), ProjectileRenderer::new);
    }
}
