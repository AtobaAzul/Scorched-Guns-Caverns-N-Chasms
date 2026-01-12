package net.atobaazul.scguns_cnc.events.client;

import net.atobaazul.scguns_cnc.client.render.gun.model.AnathemaModel;
import net.atobaazul.scguns_cnc.registries.ModEntities;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.ribs.scguns.client.render.entity.ProjectileRenderer;
import top.ribs.scguns.client.render.gun.ModelOverrides;
import top.ribs.scguns.client.render.entity.*;


import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;


@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CnCGunEntityRenderers {

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(ModEntities.HEX_ROUND_PROJECTILE.get(), ProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.BLUNTSHOT.get(), ProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.DUMMY_PROJECTILE.get(), ProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.BLOOD_SHOT.get(), ProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.COPPER_SLUG_PROJECTILE.get(), ProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.STRIKER_ROUND_PROJECTILE.get(), ProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.ENERGY_BOLT_PROJECTILE.get(), ProjectileRenderer::new);

        event.registerEntityRenderer(ModEntities.THROWABLE_MALISON_GRENADE.get(), ThrowableGrenadeRenderer::new);
    }
}
