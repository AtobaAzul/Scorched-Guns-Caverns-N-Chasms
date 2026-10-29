package net.atobaazul.scguns_cnc.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.atobaazul.scguns_cnc.SCGunsCnC;
import net.atobaazul.scguns_cnc.common.item.ShieldProjectorCurioItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.registries.ModItems.HARDLIGHT_SHIELD_PROJECTOR;
import static net.atobaazul.scguns_cnc.util.CurioUtil.getEquippedCurio;




public class ShieldBarRenderHandler {
    public static final ResourceLocation SHIELD_HEALTH_BAR_TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/shield_health_bar.png");
    public static final ResourceLocation SHIELD_HEALTH_BAR_EMPTY_TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/shield_health_empty_bar.png");
    public static final ResourceLocation SHIELD_ENERGY_BAR_TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/shield_energy_bar.png");
    public static final ResourceLocation SHIELD_ENERGY_BAR_EMPTY_TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/shield_energy_empty_bar.png");

    public static void renderShieldBar(ForgeGui gui, PoseStack poseStack, int width, int height, GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
            gui.setupOverlayRenderState(true, false);
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            if (!player.isCreative() && !player.isSpectator()) {
                ItemStack curio = getEquippedCurio(player, HARDLIGHT_SHIELD_PROJECTOR.get());

                if (curio.isEmpty()) {
                    return;
                }
                ShieldProjectorCurioItem item = (ShieldProjectorCurioItem) curio.getItem();

                float healthRatio = (float) (curio.getOrCreateTag().getDouble("health")/item.getShieldMaxHealth());
                float energyRatio = (float) item.getEnergyStored(curio)/item.getMaxEnergyStored(curio);
                ItemStack offhand = player.getOffhandItem();

                poseStack.pushPose();
                RenderSystem.depthMask(true);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                int i = width / 2;

                int energyBarXOffset = 0;
                int healthBarXOffset = -8;

                int barX = i-92-29;

                if (!offhand.isEmpty()) barX += -33;

                int barY = height-45;

                int scaledBarX = barX;
                int scaledBarY = barY;

                graphics.blit(SHIELD_HEALTH_BAR_TEXTURE, scaledBarX+healthBarXOffset, scaledBarY, 0, 0, 29, 45, 29, 45);

                int healthFillHeight = (int) Math.floor((45 * Math.abs(healthRatio-1)));
                if (healthFillHeight > 0) {
                    graphics.blit(SHIELD_HEALTH_BAR_EMPTY_TEXTURE, scaledBarX+healthBarXOffset, scaledBarY, 0, 0, 29, healthFillHeight, 29, 45);
                }


                graphics.blit(SHIELD_ENERGY_BAR_TEXTURE, scaledBarX+energyBarXOffset, scaledBarY, 0, 0, 29, 45, 29, 45);

                int energyFilLHeight = (int) Math.floor((45 * Math.abs(energyRatio-1)));
                if (energyFilLHeight > 0) {
                    graphics.blit(SHIELD_ENERGY_BAR_EMPTY_TEXTURE, scaledBarX+energyBarXOffset, scaledBarY, 0, 0, 29, energyFilLHeight, 29, 45);
                }

                RenderSystem.disableBlend();
                poseStack.popPose();
            }
        }
    }
}
