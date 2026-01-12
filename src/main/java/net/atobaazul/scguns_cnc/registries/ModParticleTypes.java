package net.atobaazul.scguns_cnc.registries;

import net.atobaazul.scguns_cnc.client.particle.EnergyBoltTrailParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MOD_ID);

    public static final RegistryObject<SimpleParticleType> ENERGY_BOLT_TRAIL = registerSimpleParticleType(true, "energy_bolt_trail");
    public static final RegistryObject<SimpleParticleType> ENERGY_BOLT_IMPACT = registerSimpleParticleType(true, "energy_bolt_impact");

    private static RegistryObject<SimpleParticleType> registerSimpleParticleType(boolean alwaysShow, String name) {
        return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(alwaysShow));
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegisterParticles {
        @SubscribeEvent
        public static void registerParticleTypes(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ENERGY_BOLT_TRAIL.get(), EnergyBoltTrailParticle.StepProvider::new);
            event.registerSpriteSet(ENERGY_BOLT_IMPACT.get(), EnergyBoltTrailParticle.Provider::new);
        }
    }
}
