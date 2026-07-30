package net.atobaazul.scguns_cnc.common.item;

import net.atobaazul.scguns_cnc.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.ribs.scguns.item.AmmoItem;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public class RicoshotRoundAmmoItem extends AmmoItem {
    private final String critChanceMultKey;
    private final String critDamageMultKey;

    public RicoshotRoundAmmoItem(Properties properties, String critChanceMultKey, String critDamageMultKey) {
        super(properties);
        this.critChanceMultKey = critChanceMultKey;
        this.critDamageMultKey = critDamageMultKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        if (this.critChanceMultKey != null && ModConfigs.COMMON.ricoshot_crit_chance.get() > 0) {
            tooltip.add(this.getChanceTooltip());
        }

        if (this.critDamageMultKey != null && ModConfigs.COMMON.ricoshot_crit_damage.get() > 0) {
            tooltip.add(this.getCritTooltip());
        }
    }

    private Component getChanceTooltip() {
        assert this.critChanceMultKey != null;

        DecimalFormat df = new DecimalFormat("#%");
        String percent = df.format(ModConfigs.COMMON.ricoshot_crit_chance.get());

        return Component.translatable(this.critChanceMultKey, percent).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    }

    private Component getCritTooltip() {
        assert this.critDamageMultKey != null;

        DecimalFormat df = new DecimalFormat("#%");
        String percent = df.format(ModConfigs.COMMON.ricoshot_crit_damage.get());

        return Component.translatable(this.critDamageMultKey, percent).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    }
}
