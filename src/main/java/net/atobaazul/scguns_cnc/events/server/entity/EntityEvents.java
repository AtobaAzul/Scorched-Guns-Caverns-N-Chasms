package net.atobaazul.scguns_cnc.events.server.entity;

import com.teamabnormals.caverns_and_chasms.common.item.SanguineArmorItem;
import net.atobaazul.scguns_cnc.common.entity.AbstractGravekeeperGunnerEntity;
import net.atobaazul.scguns_cnc.common.entity.GravekeeperHeraldEntity;
import net.atobaazul.scguns_cnc.common.item.ShieldProjectorCurioItem;
import net.atobaazul.scguns_cnc.registries.ModSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.common.ModTags.GRAVEKEEPER_ENEMY;
import static net.atobaazul.scguns_cnc.registries.ModItems.HARDLIGHT_SHIELD_PROJECTOR;
import static net.atobaazul.scguns_cnc.util.CurioUtil.getEquippedCurio;

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



    //TODO: Sounds.
    @SubscribeEvent
    public static void handleShieldAbsorption(LivingHurtEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0) {
            return;
        }

        float damage = event.getAmount();
        LivingEntity entity = event.getEntity();
        if (!event.getSource().is(DamageTypeTags.BYPASSES_ARMOR) && entity instanceof Player player) {


            ItemStack curio = getEquippedCurio(player, HARDLIGHT_SHIELD_PROJECTOR.get());

            if (!curio.isEmpty()) {

                ShieldProjectorCurioItem shield = (ShieldProjectorCurioItem) curio.getItem();

                float absorbedDamage = (float) Math.min(damage, shield.health);
                float leftoverDamage = Math.max(damage - absorbedDamage, 0);


                shield.doHealthDelta(-absorbedDamage);

                if (shield.health == 0 && leftoverDamage != damage) {
                    entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSoundEvents.HARDLIGHT_DISINTEGRATE.get(), SoundSource.PLAYERS, 1.0F, 1F);
                } else if (absorbedDamage > 0) {
                    entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSoundEvents.HARDLIGHT_HIT.get(), SoundSource.PLAYERS, 1.0F, 1F);
                }

                event.setAmount(leftoverDamage);
            }
        }
    }
}
