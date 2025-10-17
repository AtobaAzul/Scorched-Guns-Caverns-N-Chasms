package net.atobaazul.scguns_cnc.events.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.ribs.scguns.Reference;
import top.ribs.scguns.client.particle.*;
import top.ribs.scguns.init.ModParticleTypes;
import top.ribs.scguns.client.particle.CasingParticle;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)


public class ParticleRegistry {

    @SubscribeEvent
    public static void onRegisterParticleFactory(RegisterParticleProvidersEvent event)
    {

        event.registerSpriteSet(Particles.HEX_CASING_PARTICLE.get(), CasingParticle.Provider::new);

    }

}
