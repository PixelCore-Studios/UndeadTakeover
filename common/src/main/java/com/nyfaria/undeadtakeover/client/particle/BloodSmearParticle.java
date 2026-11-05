package com.nyfaria.undeadtakeover.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BloodSmearParticle extends SingleQuadParticle {
    private final Quaternionf facing;
    private final float startAlpha;

    protected BloodSmearParticle(ClientLevel level, double x, double y, double z, double normalX, double normalY, double normalZ, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        Vector3f normal = new Vector3f((float) normalX, (float) normalY, (float) normalZ);
        if (normal.lengthSquared() < 1.0E-4F) {
            normal.set(0.0F, 1.0F, 0.0F);
        }
        this.facing = new Quaternionf().rotationTo(new Vector3f(0.0F, 0.0F, 1.0F), normal.normalize());
        this.roll = this.random.nextFloat() * Mth.TWO_PI;
        this.oRoll = this.roll;
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.hasPhysics = false;
        this.gravity = 0.0F;
        this.lifetime = 100 + this.random.nextInt(60);
        this.quadSize = 0.18F + this.random.nextFloat() * 0.12F;
        float shade = 0.75F + this.random.nextFloat() * 0.25F;
        this.setColor(shade, shade, shade);
        this.startAlpha = 0.95F;
        this.alpha = this.startAlpha;
    }

    @Override
    public FacingCameraMode getFacingCameraMode() {
        return (target, camera, partialTickTime) -> target.set(this.facing);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        float fadeStart = this.lifetime * 0.6F;
        if (this.age > fadeStart) {
            this.alpha = this.startAlpha * (1.0F - (this.age - fadeStart) / (this.lifetime - fadeStart));
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new BloodSmearParticle(level, x, y, z, xAux, yAux, zAux, this.sprites.get(random));
        }
    }
}
