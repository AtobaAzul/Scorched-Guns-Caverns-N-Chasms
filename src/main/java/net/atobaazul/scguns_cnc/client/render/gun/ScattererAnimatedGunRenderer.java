package net.atobaazul.scguns_cnc.client.render.gun;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import top.ribs.scguns.client.render.gun.animated.AnimatedGunRenderer;
import top.ribs.scguns.common.ChargeHandler;
import top.ribs.scguns.item.animated.AnimatedGunItem;

import static net.atobaazul.scguns_cnc.SCGunsCnC.LOGGER;

public class ScattererAnimatedGunRenderer extends AnimatedGunRenderer {
    private ItemStack currentRenderStack;

    public ScattererAnimatedGunRenderer(ResourceLocation path) {
        super(path);
        addRenderLayer(new PackedLightAutoGlowingGeoLayer<>(this));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
        this.currentRenderStack = stack;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, AnimatedGunItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Player player = Minecraft.getInstance().player;


        if (this.currentRenderStack != null) {
            float chargeProgress = ChargeHandler.getChargeProgress(player, this.currentRenderStack);

            if (bone.getName().matches("exhaust")) {

                float startY = 0f;
                float endY = -.5f;
                float y = Mth.lerp(chargeProgress, startY, endY);

                bone.setPosY(y);
            }

            if (bone.getName().matches("charge1")) {
                int blockLight = LightTexture.block(packedLight);
                int skyLight = LightTexture.sky(packedLight);

                int light = chargeProgress > 0.33 ? 15 : 0;
                packedLight = LightTexture.pack(Mth.clamp(light + blockLight, 0, 15), Mth.clamp(light + skyLight, 0, 15));
                
                red = chargeProgress  >= 0.33 ? 1 : 0.25f;
                green = chargeProgress  >= 0.33 ? 1 : 0.25f;
                blue = chargeProgress  >= 0.33 ? 1 : 0.25f;
            }

            if (bone.getName().matches("charge2")) {
                int blockLight = LightTexture.block(packedLight);
                int skyLight = LightTexture.sky(packedLight);

                int light = chargeProgress > 0.66 ? 15 : 0;
                packedLight = LightTexture.pack(Mth.clamp(light + blockLight, 0, 15), Mth.clamp(light + skyLight, 0, 15));
             
                red = chargeProgress  >= 0.66 ? 1 : 0.25f;
                green = chargeProgress  >= 0.66 ? 1 : 0.25f;
                blue = chargeProgress  >= 0.66 ? 1 : 0.25f;
            }

            if (bone.getName().matches("charge3")) {
                int blockLight = LightTexture.block(packedLight);
                int skyLight = LightTexture.sky(packedLight);

                int light = chargeProgress >= 1 ? 15 : 0;
                packedLight = LightTexture.pack(Mth.clamp(light + blockLight, 0, 15), Mth.clamp(light + skyLight, 0, 15));
                red = chargeProgress  >= 1 ? 1 : 0.25f;
                green = chargeProgress  >= 1 ? 1 : 0.25f;
                blue = chargeProgress  >= 1 ? 1 : 0.25f;
            }
        }


        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
