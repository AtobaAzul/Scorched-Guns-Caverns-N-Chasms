package net.atobaazul.scguns_cnc.util;

import net.atobaazul.scguns_cnc.common.item.ShieldProjectorCurioItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import static net.atobaazul.scguns_cnc.registries.ModItems.HARDLIGHT_SHIELD_PROJECTOR;

public class CurioUtil {
    public static ItemStack getEquippedCurio(Player player, Item targetItem) {
        LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);
        boolean present = curiosInventory.isPresent();

        if (present) {
            for (SlotResult curio : curiosInventory.resolve().get().findCurios((stack) -> stack.is(targetItem))) {
                if (!curio.stack().isEmpty()) {
                    return curio.stack();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean hasShield(Player player) {
        ItemStack curio = getEquippedCurio(player, HARDLIGHT_SHIELD_PROJECTOR.get());
        if (!curio.isEmpty()) {
            ShieldProjectorCurioItem shield = (ShieldProjectorCurioItem) curio.getItem();
            return shield.health > 0;
        }
        return false;
    }
}
