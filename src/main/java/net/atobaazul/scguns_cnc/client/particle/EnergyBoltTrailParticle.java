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
        this.lifetime = 20;
    }

    //There has to be SOME util method for this, right????
    public static float lerp(float point1, float point2, float fraction) {
        return (1 - fraction) * point1 + fraction * point2;
    }

    @Override
    public int getLightColor(float partialTick) {
        return (int) lerp( 256, 120, (float) this.age / this.lifetime);
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
