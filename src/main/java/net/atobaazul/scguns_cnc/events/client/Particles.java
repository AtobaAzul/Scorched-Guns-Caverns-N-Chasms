package net.atobaazul.scguns_cnc.events.client;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.RegistryObject;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.ribs.scguns.particles.BulletHoleData;
import top.ribs.scguns.particles.TrailData;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;


public class Particles {

        public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MOD_ID);


        public static final RegistryObject<SimpleParticleType> HEX_CASING_PARTICLE = REGISTER.register("hex_casing", () -> new SimpleParticleType(true));
        public static final RegistryObject<SimpleParticleType> HEX_SHELL_PARTICLE = REGISTER.register("hex_shell", () -> new SimpleParticleType(true));

    }