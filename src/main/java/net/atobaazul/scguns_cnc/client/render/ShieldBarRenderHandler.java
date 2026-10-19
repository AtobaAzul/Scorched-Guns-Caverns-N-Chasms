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
import static net.atobaazul.scguns_cnc.registries.ModItems.SHIELD_PROJECTOR_BELT;
import static net.atobaazul.scguns_cnc.util.CurioUtil.getEquippedCurio;




public class ShieldBarRenderHandler {
    public static final ResourceLocation SHIELD_HEALTH_BAR_TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/shield_health_bar.png");
    public static final ResourceLocation SHIELD_ENERGY_BAR_TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/shield_energy_bar.png");

    public static void renderShieldBar(ForgeGui gui, PoseStack poseStack, int width, int height, GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
            gui.setupOverlayRenderState(true, false);
            Minecraft mc = Minecraft.getInstance();

            LocalPlayer player = minecraft.player;
            if (!player.isCreative() && !player.isSpectator()) {
                ItemStack curio = getEquippedCurio(player, SHIELD_PROJECTOR_BELT.get());
                ShieldProjectorCurioItem item = (ShieldProjectorCurioItem) curio.getItem();
                SCGunsCnC.LOGGER.info("curio {}", curio);

                SCGunsCnC.LOGGER.info("health {}", curio.getOrCreateTag().get("health"));
                SCGunsCnC.LOGGER.info("cooldown {}", curio.getOrCreateTag().get("cooldown"));
                SCGunsCnC.LOGGER.info("maxhealth {}", item.getShieldMaxHealth());
                SCGunsCnC.LOGGER.info("current energy, {}", item.getEnergyStored(curio));

                float healthRatio = (float) (curio.getOrCreateTag().getDouble("health")/item.getShieldMaxHealth());
                float energyRatio = (float) item.getEnergyStored(curio)/item.getMaxEnergyStored(curio);

                poseStack.pushPose();
                RenderSystem.setShaderTexture(0, SHIELD_HEALTH_BAR_TEXTURE);
                RenderSystem.depthMask(true);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                int barWidth = 29;
                int barHeight = 53;
                float scale = 1.25f;


                int barX = (int) (((float) width / 2) - ((barWidth * scale) / 2));
                int barY = (int) (height - 45 * scale);

                int scaledBarX = (int) (barX / scale);
                int scaledBarY = (int) (barY / scale);

                graphics.blit(SHIELD_HEALTH_BAR_TEXTURE, scaledBarX, scaledBarY, -29, 0, barWidth/2, barHeight, barWidth, barHeight);

                int fillHeight = (int) (barHeight * healthRatio);
                if (fillHeight > 0) {
                    graphics.blit(SHIELD_HEALTH_BAR_TEXTURE, scaledBarX, scaledBarY, 29, 0, barWidth/2, fillHeight, barWidth, barHeight);
                }

                RenderSystem.disableBlend();
                poseStack.popPose();
            }
        }
    }
}