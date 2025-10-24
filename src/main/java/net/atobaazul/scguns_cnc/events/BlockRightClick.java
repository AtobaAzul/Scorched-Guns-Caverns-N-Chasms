package net.atobaazul.scguns_cnc.events;

import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.SCGunsCnC.REGISTRY_HELPER;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class BlockRightClick {
    private static void print(String string) {
        System.out.println(string);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        print("event fired");
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        BlockState state = level.getBlockState(pos);
        Direction face = event.getFace();
        RandomSource random = level.getRandom();

        placeableItems:
        if (stack.is(CCItemTags.PLACEABLE_ITEMS) && !event.isCanceled()) {
            print("stack is placeable and event is not cancelled");
            boolean sneakBypassesUse = !player.getMainHandItem().doesSneakBypassUse(player.level(), pos, player) || !player.getOffhandItem().doesSneakBypassUse(player.level(), pos, player);
            boolean isSneaking = player.isSecondaryUseActive() && sneakBypassesUse;


            if (event.getUseBlock() == Event.Result.ALLOW || (event.getUseBlock() != Event.Result.DENY && !isSneaking)) {
                print("allowed, or not denied and is not sneaking");
                InteractionResult blockResult = state.use(level, player, event.getHand(), event.getHitVec());
                if (blockResult.consumesAction()) {
                    print("consumes action");
                    event.setCanceled(true);
                    event.setCancellationResult(blockResult);
                    break placeableItems;
                }
            }

            UseOnContext context = new UseOnContext(level, player, event.getHand(), stack, event.getHitVec());
            Collection<RegistryObject<Item>> items = REGISTRY_HELPER.getItemSubHelper().getDeferredRegister().getEntries();
            for (RegistryObject<Item> reg : items) {
                print("for each item registered in the item subhelper");
                if (reg.get() instanceof BlockItem blockItem && stack.is(blockItem.getBlock().asItem())) {
                    print("instance of blockitem and item is the block");
                    InteractionResult itemResult = reg.get().useOn(context);
                    if (itemResult.consumesAction()) {
                        event.setCanceled(true);
                        event.setCancellationResult(itemResult);
                    }
                    break;
                }
            }
        }
    }
}
