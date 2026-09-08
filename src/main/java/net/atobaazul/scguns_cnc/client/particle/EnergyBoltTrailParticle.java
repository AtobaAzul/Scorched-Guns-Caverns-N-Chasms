package net.atobaazul.scguns_cnc.client.particle;

import com.teamabnormals.caverns_and_chasms.client.particle.TurquoiseParticle;
import net.atobaazul.scguns_cnc.SCGunsCnC;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnergyBoltTrailParticle extends TurquoiseParticle {
    public EnergyBoltTrailParticle(ClientLevel level, boolean floor, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, floor, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime = 20;
    }



    @Override
    public int getLightColor(float partialTick) {
        int packedLight = super.getLightColor(partialTick);

        int blockLight = LightTexture.block(packedLight);
        int skyLight = LightTexture.sky(packedLight);

        int light = (int) Math.floor(Mth.lerp((float) this.age / this.lifetime, 15, 0));

        return LightTexture.pack(Mth.clamp(light + blockLight, 0, 15), Mth.clamp(light + skyLight, 0, 15));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            EnergyBoltTrailParticle particle = new EnergyBoltTrailParticle(level, false, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }

    public static class StepProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public StepProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            EnergyBoltTrailParticle particle = new EnergyBoltTrailParticle(level, true, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
