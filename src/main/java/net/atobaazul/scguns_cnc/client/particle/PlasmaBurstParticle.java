package net.atobaazul.scguns_cnc.client.particle;

import com.teamabnormals.caverns_and_chasms.client.particle.TurquoiseParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlasmaBurstParticle extends TextureSheetParticle {
    private final float starting_quad_size;

    public PlasmaBurstParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.setSize(0.02F, 0.02F);
        this.quadSize *= 1.5F;
        this.gravity = 0;
        this.lifetime = 20;
        this.starting_quad_size = this.quadSize;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


    @Override
    protected int getLightColor(float packedLight) {
        return LightTexture.FULL_BRIGHT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            PlasmaBurstParticle particle = new PlasmaBurstParticle(level,  x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return Mth.lerp((float) this.age / this.lifetime, this.starting_quad_size, 0);
    }
}
