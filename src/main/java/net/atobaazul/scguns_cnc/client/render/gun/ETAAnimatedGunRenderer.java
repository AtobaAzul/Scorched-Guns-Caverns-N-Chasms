package net.atobaazul.scguns_cnc.client.render.gun;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atobaazul.scguns_cnc.SCGunsCnC;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.object.Color;
import top.ribs.scguns.client.render.gun.animated.AnimatedGunRenderer;
import top.ribs.scguns.item.animated.AnimatedGunItem;

import java.util.Arrays;

public class ETAAnimatedGunRenderer extends AnimatedGunRenderer {
    private ItemStack currentRenderStack;

    public ETAAnimatedGunRenderer(ResourceLocation path) {
        super(path);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
        this.currentRenderStack = stack;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, AnimatedGunItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if (this.currentRenderStack != null) {
            float heat = this.currentRenderStack.getOrCreateTag().getFloat("HeatLevel") / 100;

            if ((bone.getName().matches("barrel_tip")) && heat > 0.1) {
                int blockLight = LightTexture.block(packedLight);
                int skyLight = LightTexture.sky(packedLight);

                int light = (int) Math.floor(Mth.lerp(heat-.1f, 0, 10));
                packedLight = LightTexture.pack(Mth.clamp(light + blockLight, 0, 15), Mth.clamp(light + skyLight, 0, 15));

                blue = Mth.lerp(heat-.1f, 1, 0);
                green = Mth.lerp(heat-.1f, 1, 0);
            }

            if ((bone.getName().matches("barrel_mid")) && heat > 0.2) {
                int blockLight = LightTexture.block(packedLight);
                int skyLight = LightTexture.sky(packedLight);

                int light = (int) Math.floor(Mth.lerp(heat-.2f, 0, 10));
                packedLight = LightTexture.pack(Mth.clamp(light + blockLight, 0, 15), Mth.clamp(light + skyLight, 0, 15));

                blue = Mth.lerp(heat-.2f, 1, 0.2f);
                green = Mth.lerp(heat-.2f, 1, 0.2f);
            }

            if ((bone.getName().matches("barrel_end")) && heat > 0.3) {
                int blockLight = LightTexture.block(packedLight);
                int skyLight = LightTexture.sky(packedLight);

                int light = (int) Math.floor(Mth.lerp(heat-.3f, 0, 10));
                packedLight = LightTexture.pack(Mth.clamp(light + blockLight, 0, 15), Mth.clamp(light + skyLight, 0, 15));

                blue = Mth.lerp(heat-.3f, 1, 0.2f);
                green = Mth.lerp(heat-.3f, 1, 0.2f);
            }
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public Color getRenderColor(AnimatedGunItem animatable, float partialTick, int packedLight) {
        return super.getRenderColor(animatable, partialTick, packedLight);
    }
}
