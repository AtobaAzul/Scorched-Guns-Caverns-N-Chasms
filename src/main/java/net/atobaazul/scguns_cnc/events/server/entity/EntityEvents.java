package net.atobaazul.scguns_cnc.events.server.entity;

import com.teamabnormals.caverns_and_chasms.common.item.SanguineArmorItem;
import net.atobaazul.scguns_cnc.common.entity.GravekeeperHeraldEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class EntityEvents {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();

        if (source.getEntity() instanceof GravekeeperHeraldEntity attacker) {
            float lifeStealAmount = 0.5F;
            attacker.heal(lifeStealAmount * event.getAmount());
            SanguineArmorItem.causeHealEffects(attacker, lifeStealAmount);
        }
    }
}
