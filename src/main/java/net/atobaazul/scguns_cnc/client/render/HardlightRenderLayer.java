package net.atobaazul.scguns_cnc.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atobaazul.scguns_cnc.util.CurioUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.player.Player;

public class HardlightRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final M model;

    public HardlightRenderLayer(RenderLayerParent<T, M> pRenderer, M model) {
        super(pRenderer);
        this.model = model;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int pPackedLight, T living, float limbSwing, float limbSwingAmount, float partialTicks, float age, float netHeadYaw, float headPitch) {
        if (living instanceof Player player && CurioUtil.hasShield(player)) {
            float time = (float)living.tickCount + partialTicks;
            EntityModel<T> mobModel = this.model();
            mobModel.prepareMobModel(living, limbSwing, limbSwingAmount, partialTicks);
            this.getParentModel().copyPropertiesTo(mobModel);
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset(time) % 1.0F, time * 0.01F % 1.0F));
            mobModel.setupAnim(living, limbSwing, limbSwingAmount, age, netHeadYaw, headPitch);
            mobModel.renderToBuffer(poseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
        }
    }

    protected float xOffset(float v) {
        return v * 0.01f;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected EntityModel<T> model() {
        return this.model;
    }
}
