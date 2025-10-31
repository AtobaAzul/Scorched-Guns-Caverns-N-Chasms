package net.atobaazul.scguns_cnc.common.item;


import net.atobaazul.scguns_cnc.common.entity.throwable.ThrowableMalisonGrenadeEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import top.ribs.scguns.init.ModSounds;
import top.ribs.scguns.item.AmmoItem;

public class MalisonGrenadeItem extends AmmoItem {
    protected int maxCookTime;

    public MalisonGrenadeItem(Item.Properties properties, int maxCookTime) {
        super(properties);
        this.maxCookTime = maxCookTime;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public int getUseDuration(ItemStack stack) {
        return this.maxCookTime;
    }

    public void onUseTick(Level level, LivingEntity player, ItemStack stack, int count) {
        if (this.canCook()) {
            int duration = this.getUseDuration(stack) - count;
            if (duration == 10) {
                player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), (SoundEvent)ModSounds.ITEM_GRENADE_PIN.get(), SoundSource.PLAYERS, 1.0F, 1.0F, false);
            }

        }
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        if (!worldIn.isClientSide() && playerIn instanceof Player && playerIn.isUnderWater()) {
            playerIn.awardStat(Stats.ITEM_USED.get(this));
            stack.shrink(1);
        }

        return InteractionResultHolder.consume(stack);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (this.canCook() && !worldIn.isClientSide() && !entityLiving.isUnderWater()) {
            if (!(entityLiving instanceof Player) || !((Player)entityLiving).isCreative()) {
                stack.shrink(1);
            }

            ThrowableMalisonGrenadeEntity grenade = this.create(worldIn, entityLiving, 0);
            grenade.onDeath();
            if (entityLiving instanceof Player) {
                ((Player)entityLiving).awardStat(Stats.ITEM_USED.get(this));
            }
        }

        return stack;
    }

    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (!worldIn.isClientSide() && !entityLiving.isUnderWater()) {
            int duration = this.getUseDuration(stack) - timeLeft;
            if (duration >= 10) {
                if (!(entityLiving instanceof Player) || !((Player)entityLiving).isCreative()) {
                    stack.shrink(1);
                }

                ThrowableMalisonGrenadeEntity grenade = this.create(worldIn, entityLiving, this.maxCookTime - duration);
                grenade.shootFromRotation(entityLiving, entityLiving.getXRot(), entityLiving.getYRot(), 0.0F, Math.min(1.0F, (float)duration / 10.0F), 1.0F);
                worldIn.addFreshEntity(grenade);
                this.onThrown(worldIn, grenade);
                if (entityLiving instanceof Player) {
                    ((Player)entityLiving).awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }

    }

    public ThrowableMalisonGrenadeEntity create(Level world, LivingEntity entity, int timeLeft) {
        return new ThrowableMalisonGrenadeEntity (world, entity, timeLeft);
    }

    public boolean canCook() {
        return true;
    }

    protected void onThrown(Level world, ThrowableMalisonGrenadeEntity entity) {
    }
}
