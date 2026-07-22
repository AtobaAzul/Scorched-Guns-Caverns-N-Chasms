package net.atobaazul.scguns_cnc.client.render.entity.model;

import net.atobaazul.scguns_cnc.client.entity.model.GravekeeperAcolyteModel;
import net.atobaazul.scguns_cnc.client.entity.model.GravekeeperGhoulModel;
import net.atobaazul.scguns_cnc.common.entity.GravekeeperAcolyteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;

public class GravekeeperAcolyteEntityRenderer extends GeoEntityRenderer<GravekeeperAcolyteEntity> {

    public GravekeeperAcolyteEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GravekeeperAcolyteModel());
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, GravekeeperAcolyteEntity animatable) {
                if (bone.getName().equals("item_bone")) {
                    return animatable.getMainHandItem();
                }
                return null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, GravekeeperAcolyteEntity animatable) {
                return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
            }
        });
    }





}
