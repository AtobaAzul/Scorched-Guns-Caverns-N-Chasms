package net.atobaazul.scguns_cnc.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class ShieldProjectorRender implements ICurioRenderer {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation("scguns_cnc", "hardlight_shield"), "hardlight_shield");
    private final HumanoidModel<LivingEntity> model;

    public ShieldProjectorRender(ModelPart part) {
        this.model = new HumanoidModel<>(part);
    }

    public static MeshDefinition mesh() {
        CubeListBuilder builder = new CubeListBuilder();
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        mesh.getRoot().addOrReplaceChild("body", builder, PartPose.ZERO);
        return mesh;
    }


    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource bufferSource, int light, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float v5) {
        poseStack.pushPose();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        LivingEntity entity = slotContext.entity();
        Level level = entity.level();
        BakedModel bakedModel = itemRenderer.getModel(stack, level, entity, getSeedForItemStack(stack));


        ICurioRenderer.followBodyRotations(slotContext.entity(), model);
        ICurioRenderer.rotateIfSneaking(poseStack, slotContext.entity());

        if (slotContext.entity().isCrouching()) {
            poseStack.translate(0.0F, 0.1875F, -0.125F);
        }

        poseStack.translate(0, 1, 0);

        poseStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(0)));
        poseStack.mulPose(new Quaternionf().rotationZ((float) Math.toRadians(180)));


        itemRenderer.render(stack, ItemDisplayContext.NONE, false, poseStack, bufferSource, light, OverlayTexture.NO_OVERLAY, bakedModel);
        poseStack.popPose();
    }

    private static int getSeedForItemStack(ItemStack stack) {
        return stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
    }
}

