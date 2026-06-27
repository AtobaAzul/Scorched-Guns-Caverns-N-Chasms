package net.atobaazul.scguns_cnc.client.render.entity.model;

import net.atobaazul.scguns_cnc.client.entity.model.GravekeeperGhoulModel;
import net.atobaazul.scguns_cnc.common.entity.GravekeeperGhoulEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GravekeeperGhoulEntityRenderer extends GeoEntityRenderer<GravekeeperGhoulEntity> {
    public GravekeeperGhoulEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GravekeeperGhoulModel());
    }
}
