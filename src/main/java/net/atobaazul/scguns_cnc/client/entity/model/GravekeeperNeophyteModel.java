package net.atobaazul.scguns_cnc.client.entity.model;

import net.atobaazul.scguns_cnc.common.entity.GravekeeperNeophyteEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class GravekeeperNeophyteModel extends GeoModel<GravekeeperNeophyteEntity> {
    private final ResourceLocation model = new ResourceLocation(MOD_ID, "geo/entity/gravekeeper_neophyte.geo.json");
    private final ResourceLocation texture = new ResourceLocation(MOD_ID, "textures/entity/gravekeeper_neophyte.png");
    private final ResourceLocation animations = new ResourceLocation(MOD_ID, "animations/entity/gravekeeper_neophyte.animation.json");


    @Override
    public ResourceLocation getModelResource(GravekeeperNeophyteEntity gravekeeperNeophyteEntity) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(GravekeeperNeophyteEntity gravekeeperNeophyteEntity) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(GravekeeperNeophyteEntity gravekeeperNeophyteEntity) {
        return this.animations;
    }

    @Override
    public void setCustomAnimations(GravekeeperNeophyteEntity animatable, long instanceId, AnimationState<GravekeeperNeophyteEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("Head");
        CoreGeoBone arms = getAnimationProcessor().getBone("arms");
        CoreGeoBone waist = getAnimationProcessor().getBone("Waist");


        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null) {
            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }

        if (arms != null && waist != null) {

            //if (animationState.isCurrentAnimation(IDLE_ALERT) || animationState.isCurrentAnimation(WALK_ALERT) || animationState.isCurrentAnimation(SHOOT))
            //{
                arms.setRotX(((entityData.headPitch()) * Mth.DEG_TO_RAD)* 1f);
                arms.setRotY(((entityData.netHeadYaw()) * Mth.DEG_TO_RAD)* 0.5f);

                waist.setRotX(((entityData.headPitch()) * Mth.DEG_TO_RAD)* 0.5f);
                waist.setRotY(((entityData.netHeadYaw()) * Mth.DEG_TO_RAD)* 0.5f);

                if (head != null) {
                    //cancel out effects of waist moving with head
                    float wX = waist.getRotX();
                    float wY = waist.getRotY();

                    float hX = head.getRotX();
                    float hY = head.getRotY();

                    head.setRotX(hX-wX);
                    head.setRotY(hY-wY);
                }
            //} else {
            //    arms.setRotX(0);
            //    arms.setRotY(0);
            //    waist.setRotX(0);
            //}
        }
    }
}
