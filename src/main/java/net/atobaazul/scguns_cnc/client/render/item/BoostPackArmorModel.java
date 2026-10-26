package net.atobaazul.scguns_cnc.client.render.item;

import net.atobaazul.scguns_cnc.SCGunsCnC;
import net.atobaazul.scguns_cnc.common.item.BoostPackArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import top.ribs.scguns.Reference;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class BoostPackArmorModel extends GeoModel<BoostPackArmorItem> {


    @Override
    public void setCustomAnimations(BoostPackArmorItem animatable, long instanceId, AnimationState<BoostPackArmorItem> animationState) {

        Entity entity = animationState.getData(DataTickets.ENTITY);

        float xRot = entity.getXRot()/45;

        GeoBone thruster_2 = (GeoBone) getAnimationProcessor().getBone("thruster_2");
        GeoBone thruster_1 = (GeoBone) getAnimationProcessor().getBone("thruster_1");

        thruster_1.setRotX(xRot*-1);
        thruster_2.setRotX(xRot*-1);
    }

    @Override
    public ResourceLocation getModelResource(BoostPackArmorItem animatable) {
        return new ResourceLocation(MOD_ID, "geo/boost_pack.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BoostPackArmorItem animatable) {
        return new ResourceLocation(MOD_ID, "textures/armor/boost_pack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BoostPackArmorItem animatable) {
        return new ResourceLocation(MOD_ID, "animations/boost_pack.animation.json");
    }
}
