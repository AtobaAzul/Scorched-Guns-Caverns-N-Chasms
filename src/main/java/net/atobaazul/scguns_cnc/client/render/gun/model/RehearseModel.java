package net.atobaazul.scguns_cnc.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.atobaazul.scguns_cnc.events.client.CCSpecialModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.ribs.scguns.client.SpecialModels;
import top.ribs.scguns.client.render.gun.IOverrideModel;
import top.ribs.scguns.client.util.RenderUtil;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.init.ModItems;
import top.ribs.scguns.item.attachment.IAttachment;


public class RehearseModel implements IOverrideModel {
    @SuppressWarnings("resource")
    @Override

    public void render(float partialTicks, ItemDisplayContext transformType, ItemStack stack, ItemStack parent, LivingEntity entity, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        RenderUtil.renderModel(CCSpecialModels.REHEARSE_MAIN.getModel(), stack, matrixStack, buffer, light, overlay);

        if (Gun.hasAttachmentEquipped(stack, IAttachment.Type.STOCK)) {
            if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.WEIGHTED_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_STOCK_WEIGHTED.getModel(), stack, matrixStack, buffer, light, overlay);
            if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.LIGHT_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_STOCK_LIGHT.getModel(), stack, matrixStack, buffer, light, overlay);
            if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.WOODEN_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_STOCK_WOODEN.getModel(), stack, matrixStack, buffer, light, overlay);
            if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.BUMP_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_STOCK_WEIGHTED.getModel(), stack, matrixStack, buffer, light, overlay);

        }

        renderBarrelAttachments(matrixStack, buffer, stack, light, overlay);

        float cooldown = 0.0F;
        if (entity.equals(Minecraft.getInstance().player)) {
            ItemCooldowns tracker = Minecraft.getInstance().player.getCooldowns();
            cooldown = tracker.getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime());
            cooldown = (float) ease(cooldown);
        }
        matrixStack.pushPose();
        matrixStack.translate(0, -0.28, 0.36);
        float rotationAngle = -cooldown * 38;
        matrixStack.mulPose(Axis.XP.rotationDegrees(rotationAngle));
        matrixStack.translate(0, 0.28, -0.36);
        RenderUtil.renderModel(SpecialModels.SEQUOIA_HAMMER.getModel(), stack, matrixStack, buffer, light, overlay);
        matrixStack.popPose();

        RenderUtil.renderModel(CCSpecialModels.REHEARSE_DRUM.getModel(), stack, matrixStack, buffer, light, overlay);
    }

    private void renderBarrelAttachments(PoseStack matrixStack, MultiBufferSource buffer, ItemStack stack, int light, int overlay) {
        boolean hasExtendedBarrel = false;

        if (Gun.hasAttachmentEquipped(stack, IAttachment.Type.BARREL)) {
            if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.EXTENDED_BARREL.get()) {
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_EXT_BARREL.getModel(), stack, matrixStack, buffer, light, overlay);
                hasExtendedBarrel = true;
            } else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.SILENCER.get()) {
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_SILENCER.getModel(), stack, matrixStack, buffer, light, overlay);
            } else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_BRAKE.get()) {
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_MUZZLE_BRAKE.getModel(), stack, matrixStack, buffer, light, overlay);
            } else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.ADVANCED_SILENCER.get()) {
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_ADVANCED_SILENCER.getModel(), stack, matrixStack, buffer, light, overlay);
            }
        }

        if (!hasExtendedBarrel) {
            RenderUtil.renderModel(CCSpecialModels.REHEARSE_STAN_BARREL.getModel(), stack, matrixStack, buffer, light, overlay);
        }
    }
    private double ease(double x) {
        return 1 - Math.pow(1 - x, 4);
    }

}