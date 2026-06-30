package net.atobaazul.scguns_cnc.client.render.entity.model;

import net.atobaazul.scguns_cnc.client.entity.model.GravekeeperNeophyteModel;
import net.atobaazul.scguns_cnc.common.entity.GravekeeperNeophyteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;

public class GravekeeperNeophyteEntityRenderer extends GeoEntityRenderer<GravekeeperNeophyteEntity> {

    public GravekeeperNeophyteEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GravekeeperNeophyteModel());
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, GravekeeperNeophyteEntity animatable) {
                if (bone.getName().equals("item_bone")) {
                    return animatable.getMainHandItem();
                }
                return null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, GravekeeperNeophyteEntity animatable) {
                return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
            }
        });
    }
}
