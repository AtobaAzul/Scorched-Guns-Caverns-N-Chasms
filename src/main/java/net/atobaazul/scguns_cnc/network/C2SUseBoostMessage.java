package net.atobaazul.scguns_cnc.network;

import com.mrcrayfish.framework.api.network.LevelLocation;
import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import net.atobaazul.scguns_cnc.ModConfigs;
import net.atobaazul.scguns_cnc.common.item.BoostPackArmorItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import static net.atobaazul.scguns_cnc.registries.ModItems.BOOST_PACK;

public class C2SUseBoostMessage extends PlayMessage<C2SUseBoostMessage> {
    public C2SUseBoostMessage() {
    }


    @Override
    public void encode(C2SUseBoostMessage c2SUseBoostMessage, FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public C2SUseBoostMessage decode(FriendlyByteBuf friendlyByteBuf) {
        return new C2SUseBoostMessage();
    }

    @Override
    public void handle(C2SUseBoostMessage message, MessageContext context) {
        context.execute(() -> {
            ServerPlayer player = context.getPlayer();
            if (player == null) return;

            player.getCooldowns().addCooldown(BOOST_PACK.get(), ModConfigs.COMMON.boost_pack_refill_cd.get());

            for (ItemStack armorSlot : player.getArmorSlots()) {
                if (armorSlot.is(BOOST_PACK.get())) {
                    BoostPackArmorItem item = (BoostPackArmorItem) armorSlot.getItem();
                    item.setCharged(armorSlot, false);
                    S2CBoostSoundMessage _message = new S2CBoostSoundMessage(player.getId());
                    PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(player.level(), player.getX(), player.getY(), player.getZ(), 32), _message);
                    context.setHandled(true);
                    break;
                }
            }
        });
    }
}
