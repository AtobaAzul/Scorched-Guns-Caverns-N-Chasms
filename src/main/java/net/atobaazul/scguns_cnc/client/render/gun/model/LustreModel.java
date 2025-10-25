package net.atobaazul.scguns_cnc.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atobaazul.scguns_cnc.events.client.CCSpecialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.ribs.scguns.client.render.gun.IOverrideModel;
import top.ribs.scguns.client.util.RenderUtil;


public class LustreModel implements IOverrideModel {
    @SuppressWarnings("resource")
    @Override
    public void render(float partialTicks, ItemDisplayContext transformType, ItemStack stack, ItemStack parent, LivingEntity entity, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {

        RenderUtil.renderModel(CCSpecialModels.LUSTRE_MAIN.getModel(), stack, matrixStack, buffer, light, overlay);


    }
}
