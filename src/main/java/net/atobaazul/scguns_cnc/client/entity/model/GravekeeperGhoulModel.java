package net.atobaazul.scguns_cnc.client.entity.model;

import net.atobaazul.scguns_cnc.common.entity.GravekeeperGhoulEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class GravekeeperGhoulModel extends GeoModel<GravekeeperGhoulEntity> {
    private final ResourceLocation model = new ResourceLocation(MOD_ID, "geo/entity/gravekeeper_ghoul.geo.json");
    private final ResourceLocation texture = new ResourceLocation(MOD_ID, "textures/entity/gravekeeper_ghoul.png");
    private final ResourceLocation animations = new ResourceLocation(MOD_ID, "animations/entity/gravekeeper_ghoul.animation.json");


    @Override
    public ResourceLocation getModelResource(GravekeeperGhoulEntity gravekeeperGhoulEntity) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(GravekeeperGhoulEntity gravekeeperGhoulEntity) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(GravekeeperGhoulEntity gravekeeperGhoulEntity) {
        return this.animations;
    }


}
