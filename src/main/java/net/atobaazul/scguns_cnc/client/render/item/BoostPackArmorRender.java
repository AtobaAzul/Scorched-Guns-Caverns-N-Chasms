package net.atobaazul.scguns_cnc.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atobaazul.scguns_cnc.common.item.BoostPackArmorItem;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BoostPackArmorRender extends GeoArmorRenderer<BoostPackArmorItem> {
    public <I extends BoostPackArmorItem> BoostPackArmorRender() {
        super(new BoostPackArmorModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, BoostPackArmorItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().matches(".*light.*")) {
            packedLight = LightTexture.FULL_BRIGHT;
            packedOverlay = LightTexture.FULL_BRIGHT;
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
