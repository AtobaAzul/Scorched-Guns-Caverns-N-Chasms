package net.atobaazul.scguns_cnc.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atobaazul.scguns_cnc.events.client.CCSpecialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.ribs.scguns.client.render.gun.IOverrideModel;
import top.ribs.scguns.client.util.RenderUtil;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.init.ModItems;
import top.ribs.scguns.item.attachment.IAttachment;


public class IronPartisanModel implements IOverrideModel {
    @SuppressWarnings("resource")
    @Override
    public void render(float partialTicks, ItemDisplayContext transformType, ItemStack stack, ItemStack parent, LivingEntity entity, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {

        // Renders the static parts of the model.
        RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_MAIN.getModel(), stack, matrixStack, buffer, light, overlay);

        // Render stock attachments
        renderStockAttachments(stack, matrixStack, buffer, light, overlay);



        // Render magazine attachments
        renderMagazineAttachments(stack, matrixStack, buffer, light, overlay);

        // Render barrel and attachments with the new system
        renderBarrelAndAttachments(stack, matrixStack, buffer, light, overlay);
    }

    private void renderStockAttachments(ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        if (Gun.hasAttachmentEquipped(stack, IAttachment.Type.STOCK)) {
            if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.WOODEN_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_STOCK_WOODEN.getModel(), stack, matrixStack, buffer, light, overlay);
            else if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.LIGHT_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_STOCK_LIGHT.getModel(), stack, matrixStack, buffer, light, overlay);
            else if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.WEIGHTED_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_STOCK_WEIGHTED.getModel(), stack, matrixStack, buffer, light, overlay);
            else if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.BUMP_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_STOCK_WEIGHTED.getModel(), stack, matrixStack, buffer, light, overlay);
        }
    }



    private void renderMagazineAttachments(ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        if (Gun.hasAttachmentEquipped(stack, IAttachment.Type.MAGAZINE)) {
            if (Gun.getAttachment(IAttachment.Type.MAGAZINE, stack).getItem() == ModItems.EXTENDED_MAG.get())
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_EXT_MAGAZINE.getModel(), stack, matrixStack, buffer, light, overlay);
            if (Gun.getAttachment(IAttachment.Type.MAGAZINE, stack).getItem() == ModItems.SPEED_MAG.get())
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_FAST_MAGAZINE.getModel(), stack, matrixStack, buffer, light, overlay);
            if (Gun.getAttachment(IAttachment.Type.MAGAZINE, stack).getItem() == ModItems.PLUS_P_MAG.get())
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_EXT_MAGAZINE.getModel(), stack, matrixStack, buffer, light, overlay);
        } else {
            RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_STAN_MAGAZINE.getModel(), stack, matrixStack, buffer, light, overlay);
        }
    }

    private void renderBarrelAndAttachments(ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        boolean hasExtendedBarrel = false;

        if (Gun.hasAttachmentEquipped(stack, IAttachment.Type.BARREL)) {
            if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.EXTENDED_BARREL.get()) {
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_EXT_BARREL.getModel(), stack, matrixStack, buffer, light, overlay);
                hasExtendedBarrel = true;
            } else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.SILENCER.get())
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_SILENCER.getModel(), stack, matrixStack, buffer, light, overlay);
            else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.ADVANCED_SILENCER.get())
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_ADVANCED_SILENCER.getModel(), stack, matrixStack, buffer, light, overlay);
            else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_BRAKE.get())
                RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_MUZZLE_BRAKE.getModel(), stack, matrixStack, buffer, light, overlay);
        }
        if (!hasExtendedBarrel) {
            RenderUtil.renderModel(CCSpecialModels.IRON_PARTISAN_STAN_BARREL.getModel(), stack, matrixStack, buffer, light, overlay);
        }
    }
}