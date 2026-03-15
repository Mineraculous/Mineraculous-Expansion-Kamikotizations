package dev.thomasglasser.mineraculouskamikotizations.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

public class FloatingBubbleParticle extends TextureSheetParticle {
    protected final SpriteSet sprites;
    private final float startR, startG, startB;
    private final float wobbleOffset;
    private final boolean limitedShift;
    private final float baseHue;

    public FloatingBubbleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, FloatingBubbleParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.lifetime = options.lifetime();
        this.startR = options.r();
        this.startG = options.g();
        this.startB = options.b();
        this.rCol = startR;
        this.gCol = startG;
        this.bCol = startB;
        this.wobbleOffset = level.random.nextFloat() * (float) Math.PI * 2;
        this.xd += (level.random.nextFloat() - 0.5f) * 0.1f;
        this.zd += (level.random.nextFloat() - 0.5f) * 0.1f;
        this.setSpriteFromAge(sprites);
        this.scale(level.random.nextFloat() * 0.5f + 0.5f);
        this.hasPhysics = false;
        this.limitedShift = options.limitedShift();
        float[] hsb = java.awt.Color.RGBtoHSB(
                (int) (options.r() * 255),
                (int) (options.g() * 255),
                (int) (options.b() * 255), null);
        this.baseHue = hsb[0];
    }

    private void updateColorAndAlpha(float progress) {
        float hue;
        if (limitedShift) {
            // Shift only slightly (±0.1 on the 0-1 hue scale)
            float shift = Mth.sin(progress * (float) Math.PI) * -0.1f;
            hue = (baseHue + shift + 1.0f) % 1.0f;
        } else {
            hue = (progress * 2.5f) % 1.0f;
        }

        float saturation = 0.6f + (Mth.sin(this.age * 0.1f) * 0.2f);
        int color = Mth.hsvToRgb(hue, saturation, 1.0f);

        this.rCol = (float) (color >> 16 & 255) / 255.0F;
        this.gCol = (float) (color >> 8 & 255) / 255.0F;
        this.bCol = (float) (color & 255) / 255.0F;

        if (progress >= 0.9F) {
            float popProgress = (progress - 0.9F) / 0.1F;
            this.alpha = Mth.lerp(popProgress, 1.0F, 0.0F);
        }
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            float wobbleSpeed = 0.15f;
            float wobbleStrength = 0.015f;

            this.xd += Math.cos(this.age * wobbleSpeed + wobbleOffset) * wobbleStrength;
            this.zd += Math.sin(this.age * wobbleSpeed + wobbleOffset) * wobbleStrength;

            this.yd += 0.005f * (Math.random() > 0.5 ? -1 : 1);

            this.xd *= 0.96f;
            this.yd *= 0.96f;
            this.zd *= 0.96f;

            this.move(this.xd, this.yd, this.zd);

            float progress = (float) this.age / (float) this.lifetime;
            updateColorAndAlpha(progress * 0.5f);
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public void setSpriteFromAge(SpriteSet sprite) {
        if (!this.removed) {
            float popThreshold = 0.9F;
            float progress = (float) this.age / (float) this.lifetime;
            if (progress < popThreshold) {
                this.setSprite(sprite.get(0, 1));
            } else {
                float popProgress = (progress - popThreshold) / (1.0F - popThreshold);
                int spriteIndex = Mth.clamp((int) (popProgress * 5), 0, 4);
                this.setSprite(sprite.get(spriteIndex + 1, 5));
                this.alpha = 1.0F - popProgress;
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<FloatingBubbleParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(FloatingBubbleParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FloatingBubbleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type, sprites);
        }
    }
}
