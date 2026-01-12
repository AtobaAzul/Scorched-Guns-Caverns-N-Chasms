package net.atobaazul.scguns_cnc.client.particle;

import com.teamabnormals.caverns_and_chasms.client.particle.TurquoiseParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnergyBoltTrailParticle extends TurquoiseParticle {
    public EnergyBoltTrailParticle(ClientLevel level, boolean floor, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, floor, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime = (int) (this.random.nextInt(floor ? 60 : 20) + (floor ? 60 : 20)*0.125);

    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
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
