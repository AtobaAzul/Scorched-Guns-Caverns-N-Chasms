package net.atobaazul.scguns_cnc.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atobaazul.scguns_cnc.common.item.BoostPackArmorItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BoostPackArmorRender extends GeoArmorRenderer<BoostPackArmorItem> {
    public <I extends BoostPackArmorItem> BoostPackArmorRender() {
        super(new BoostPackArmorModel());
    }


}
