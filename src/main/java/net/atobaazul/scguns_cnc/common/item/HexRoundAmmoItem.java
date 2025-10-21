package net.atobaazul.scguns_cnc.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.ribs.scguns.item.AmmoItem;

import javax.annotation.Nullable;
import java.util.List;

public class HexRoundAmmoItem extends AmmoItem {
    public HexRoundAmmoItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.hex_round.magic_damage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}
