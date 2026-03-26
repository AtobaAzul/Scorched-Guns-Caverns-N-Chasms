package net.atobaazul.scguns_cnc.client.render.gun;

import net.minecraft.resources.ResourceLocation;
import top.ribs.scguns.client.render.gun.animated.AnimatedGunRenderer;

public class EmmisiveAnimatedGunRenderer extends AnimatedGunRenderer {
    public EmmisiveAnimatedGunRenderer(ResourceLocation path) {
        super(path);
        addRenderLayer(new PackedLightAutoGlowingGeoLayer<>(this));
    }
}
