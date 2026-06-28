package net.atobaazul.scguns_cnc.client.entity.model;

import net.atobaazul.scguns_cnc.common.entity.GravekeeperGhoulEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import javax.swing.text.html.parser.Entity;

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

    @Override
    public void setCustomAnimations(GravekeeperGhoulEntity animatable, long instanceId, AnimationState<GravekeeperGhoulEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("Head");
        CoreGeoBone arms = getAnimationProcessor().getBone("arms");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }

        if (arms != null ) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            //if (!(animationState.isCurrentAnimation(DefaultAnimations.IDLE))) {
                arms.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
                arms.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
            //} else {
            //    arms.setRotX(0);
            //    arms.setRotY(0);
            //}
        }
    }
}
