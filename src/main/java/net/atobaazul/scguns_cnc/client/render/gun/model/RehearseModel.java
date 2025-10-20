package net.atobaazul.scguns_cnc.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atobaazul.scguns_cnc.events.client.CCSpecialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
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

        boolean extendedBarrelAttached = false;


        RenderUtil.renderModel(CCSpecialModels.REHEARSE_MAIN.getModel(), stack, matrixStack, buffer, light, overlay);
        RenderUtil.renderModel(CCSpecialModels.REHEARSE_DRUM.getModel(), stack, matrixStack, buffer, light, overlay);

        RenderUtil.renderModel(CCSpecialModels.REHEARSE_STAN_BARREL.getModel(), stack, matrixStack, buffer, light, overlay);

        if (Gun.hasAttachmentEquipped(stack, IAttachment.Type.STOCK)) {
            if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.WOODEN_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_STOCK_WOODEN.getModel(), stack, matrixStack, buffer, light, overlay);
            else if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.LIGHT_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_STOCK_LIGHT.getModel(), stack, matrixStack, buffer, light, overlay);
            else if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.WEIGHTED_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_STOCK_WEIGHTED.getModel(), stack, matrixStack, buffer, light, overlay);
            else if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.BUMP_STOCK.get())
                RenderUtil.renderModel(CCSpecialModels.REHEARSE_STOCK_WEIGHTED.getModel(), stack, matrixStack, buffer, light, overlay);

        } else {
            RenderUtil.renderModel(CCSpecialModels.REHEARSE_STAN_GRIP.getModel(), stack, matrixStack, buffer, light, overlay);
        }
       

    }


    }
