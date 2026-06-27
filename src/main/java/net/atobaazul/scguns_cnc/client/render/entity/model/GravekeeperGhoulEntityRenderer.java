package net.atobaazul.scguns_cnc.client.render.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atobaazul.scguns_cnc.client.entity.model.GravekeeperGhoulModel;
import net.atobaazul.scguns_cnc.client.render.gun.PackedLightAutoGlowingGeoLayer;
import net.atobaazul.scguns_cnc.common.entity.GravekeeperGhoulEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;
import java.util.Objects;

public class GravekeeperGhoulEntityRenderer extends GeoEntityRenderer<GravekeeperGhoulEntity> {

    public GravekeeperGhoulEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GravekeeperGhoulModel());
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, GravekeeperGhoulEntity animatable) {
                if (bone.getName().equals("item_bone")) {
                    return animatable.getMainHandItem();
                }
                return null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, GravekeeperGhoulEntity animatable) {
                return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
            }
        });
    }
}
