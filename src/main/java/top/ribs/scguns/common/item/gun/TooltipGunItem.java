package top.ribs.scguns.common.item.gun;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.ribs.scguns.item.animated.AnimatedGunItem;

import javax.annotation.Nullable;
import java.util.List;

//You might be asking yourself "why is this file not under the net.atobaazul.scguns_cnc package?!"
//Well, that's because of several things regarding reload and casing ejection packets EXPLICITLY requiring that the
//gun item's class be under the 'top.ribs.scguns' packages! FOR SOME REASON!!!
public class TooltipGunItem extends AnimatedGunItem {
    private final String tooltipKey;
    private final String secondaryTooltipKey;

    public TooltipGunItem(Properties properties, String path, SoundEvent reloadSoundMagOut, SoundEvent reloadSoundMagIn, SoundEvent reloadSoundEnd, SoundEvent boltPullSound, SoundEvent boltReleaseSound, String tooltipKey, @Nullable String secondaryTooltipKey) {
        super(properties, path, reloadSoundMagOut, reloadSoundMagIn, reloadSoundEnd, boltPullSound, boltReleaseSound);
        this.tooltipKey = tooltipKey;
        this.secondaryTooltipKey = secondaryTooltipKey;
    }

    public TooltipGunItem(Properties properties, String path, SoundEvent reloadSoundMagOut, SoundEvent reloadSoundMagIn, SoundEvent reloadSoundEnd, SoundEvent boltPullSound, SoundEvent boltReleaseSound, String tooltipKey) {
        super(properties, path, reloadSoundMagOut, reloadSoundMagIn, reloadSoundEnd, boltPullSound, boltReleaseSound);
        this.tooltipKey = tooltipKey;
        this.secondaryTooltipKey = null;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (this.tooltipKey != null && !this.tooltipKey.isEmpty()) {
            tooltip.add(Component.translatable(this.tooltipKey).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }

        if (this.secondaryTooltipKey != null && !this.secondaryTooltipKey.isEmpty()) {
            tooltip.add(Component.translatable(this.secondaryTooltipKey).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
