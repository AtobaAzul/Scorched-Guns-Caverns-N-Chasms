package net.atobaazul.scguns_cnc.client.render.entity.model;

import net.atobaazul.scguns_cnc.client.entity.model.GravekeeperSchismModel;
import net.atobaazul.scguns_cnc.common.entity.GravekeeperSchismEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;

public class GravekeeperSchismEntityRenderer extends GeoEntityRenderer<GravekeeperSchismEntity> {

    public GravekeeperSchismEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GravekeeperSchismModel());
        //mainhand render layer
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, GravekeeperSchismEntity animatable) {
                if (bone.getName().equals("item_bone")) {
                    return animatable.getMainHandItem();
                }

                return null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, GravekeeperSchismEntity animatable) {
                return bone.getName().equals("item_bone") ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.NONE;
            }
        });
        //offhand render layer
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, GravekeeperSchismEntity animatable) {
                if (bone.getName().equals("offhand_item_bone")) {
                    return animatable.getOffhandItem();
                }
                return null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, GravekeeperSchismEntity animatable) {
                return bone.getName().equals("offhand_item_bone") ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.NONE;
            }
        });
    }
}
