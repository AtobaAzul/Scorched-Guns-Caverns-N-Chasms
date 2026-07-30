package net.atobaazul.scguns_cnc.common.item;

import net.atobaazul.scguns_cnc.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import top.ribs.scguns.item.AmmoItem;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public class ConfigTooltipAmmoItem extends AmmoItem {
    private final String descriptionKey;
    private final ForgeConfigSpec.DoubleValue config;

    public ConfigTooltipAmmoItem(Properties properties, String descriptionKey, ForgeConfigSpec.DoubleValue config) {
        super(properties);
        this.descriptionKey = descriptionKey;
        this.config = config;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        if (this.descriptionKey != null && config.get() > 0) {
            tooltip.add(this.getDescriptionTooltip());
        }
    }

    private Component getDescriptionTooltip() {
        assert this.descriptionKey != null;

        DecimalFormat df = new DecimalFormat("#%");
        String percent = df.format(config.get());

        return Component.translatable(this.descriptionKey, percent).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    }
}
