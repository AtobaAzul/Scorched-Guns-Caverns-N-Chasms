package net.atobaazul.scguns_cnc.client.render.entity.model;

import net.atobaazul.scguns_cnc.client.entity.model.GravekeeperHeraldModel;
import net.atobaazul.scguns_cnc.common.entity.GravekeeperHeraldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;

public class GravekeeperHeraldEntityRenderer extends GeoEntityRenderer<GravekeeperHeraldEntity> {

    public GravekeeperHeraldEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GravekeeperHeraldModel());
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, GravekeeperHeraldEntity animatable) {
                if (bone.getName().equals("item_bone")) {
                    return animatable.getMainHandItem();
                }
                return null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, GravekeeperHeraldEntity animatable) {
                return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
            }
        });
    }
}
