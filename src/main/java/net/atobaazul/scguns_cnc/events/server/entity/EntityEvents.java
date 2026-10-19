package net.atobaazul.scguns_cnc.events.server.entity;

import com.teamabnormals.caverns_and_chasms.common.item.SanguineArmorItem;
import net.atobaazul.scguns_cnc.common.entity.AbstractGravekeeperGunnerEntity;
import net.atobaazul.scguns_cnc.common.entity.GravekeeperHeraldEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.common.ModTags.GRAVEKEEPER_ENEMY;

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

    @SubscribeEvent
    public static void onEntityJoinLevelEvent(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (event.getEntity() instanceof Mob mob && event.getEntity().getType().is(GRAVEKEEPER_ENEMY) && mob.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)) {
            mob.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(mob, AbstractGravekeeperGunnerEntity.class, false));
        }
    }
}
