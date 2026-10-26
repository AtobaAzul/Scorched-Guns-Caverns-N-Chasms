package net.atobaazul.scguns_cnc.network;

import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.network.FrameworkNetwork;
import com.mrcrayfish.framework.api.network.MessageDirection;
import net.minecraft.resources.ResourceLocation;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class PacketHandler {
    private static FrameworkNetwork playChannel;

    public static void init() {
        playChannel = FrameworkAPI.createNetworkBuilder(new ResourceLocation(MOD_ID, "play"), 1)
                .registerPlayMessage(C2SUseBoostMessage.class, MessageDirection.PLAY_SERVER_BOUND)
                .registerPlayMessage(S2CBoostSoundMessage.class, MessageDirection.PLAY_CLIENT_BOUND)
                .build();

    }

    public static FrameworkNetwork getPlayChannel()
    {
        return playChannel;
    }

}
