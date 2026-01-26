package net.atobaazul.scguns_cnc.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import top.ribs.scguns.event.GunFireEvent;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class GunEvents {
    @SubscribeEvent
    public static void preShoot(GunFireEvent.Pre event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();

        if (!player.isCreative()) {
            if ((heldItem.getItem() instanceof RechargeableEnergyGunItem) && player.isUnderWater()) {
                event.setCanceled(true);
            }
        }
    }
}
