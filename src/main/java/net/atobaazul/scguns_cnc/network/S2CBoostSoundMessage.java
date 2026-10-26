package net.atobaazul.scguns_cnc.network;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import net.atobaazul.scguns_cnc.registries.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class S2CBoostSoundMessage extends PlayMessage<S2CBoostSoundMessage> {
    private final int entityId;

    public S2CBoostSoundMessage(int entityId) {
        this.entityId = entityId;
    }

    public S2CBoostSoundMessage() {
        this.entityId = -1;
    }

    @Override
    public void encode(S2CBoostSoundMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.entityId);
    }

    @Override
    public S2CBoostSoundMessage decode(FriendlyByteBuf buffer) {
        int shooterId = buffer.readInt();

        return new S2CBoostSoundMessage(shooterId);
    }

    @Override
    public void handle(S2CBoostSoundMessage message, MessageContext context) {
        context.execute(() -> {
            handleSound(message);
            context.setHandled(true);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void handleSound(S2CBoostSoundMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        Entity entity = mc.level.getEntity(message.entityId);

        if (entity != null) {
            Minecraft.getInstance().getSoundManager().play(new EntityBoundSoundInstance(ModSoundEvents.BOOST_PACK_THRUST.get(), SoundSource.PLAYERS, 1.0f, 1.0f, entity, mc.level.getRandom().nextLong()));
        }
    }
}
