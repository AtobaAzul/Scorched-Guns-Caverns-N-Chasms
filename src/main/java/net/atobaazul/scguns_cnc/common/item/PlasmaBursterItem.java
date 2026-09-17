package net.atobaazul.scguns_cnc.common.item;


import net.atobaazul.scguns_cnc.common.entity.projectile.throwable.ThrowablePlasmaBursterEntity;
import net.atobaazul.scguns_cnc.registries.ModSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import top.ribs.scguns.item.AmmoItem;

public class PlasmaBursterItem extends AmmoItem {
    protected int maxCookTime;

    public PlasmaBursterItem(Properties properties, int maxCookTime) {
        super(properties);
        this.maxCookTime = maxCookTime;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public int getUseDuration(ItemStack stack) {
        return this.maxCookTime;
    }


    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
        player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), ModSoundEvents.SCATTERER_CHARGE.get(), SoundSource.PLAYERS, 1.0F, 1.0F, false);

        ItemStack stack = player.getItemInHand(handIn);
        player.startUsingItem(handIn);
        if (!worldIn.isClientSide() && player instanceof Player && player.isUnderWater()) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.consume(stack);
    }


    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (!worldIn.isClientSide() && !entityLiving.isUnderWater()) {
            int duration = this.getUseDuration(stack) - timeLeft;
            if (duration >= 10) {
                if (!(entityLiving instanceof Player) || !((Player) entityLiving).isCreative()) {
                    stack.shrink(1);
                }

                ThrowablePlasmaBursterEntity grenade = this.create(worldIn, entityLiving, this.maxCookTime);
                grenade.shootFromRotation(entityLiving, entityLiving.getXRot(), entityLiving.getYRot(), 0.0F, Math.min(1.0F, (float) duration / 10.0F), 1.0F);
                worldIn.addFreshEntity(grenade);
                this.onThrown(worldIn, grenade);
                if (entityLiving instanceof Player) {
                    ((Player) entityLiving).awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }

    }

    public ThrowablePlasmaBursterEntity create(Level world, LivingEntity entity, int timeLeft) {
        return new ThrowablePlasmaBursterEntity(world, entity, timeLeft);
    }

    public boolean canCook() {
        return false;
    }

    protected void onThrown(Level world, ThrowablePlasmaBursterEntity entity) {
    }
}
