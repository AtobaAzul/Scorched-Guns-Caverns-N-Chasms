package net.atobaazul.scguns_cnc.events;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import top.ribs.scguns.common.item.gun.RechargeableEnergyGunItem;
import top.ribs.scguns.init.ModCreativeModeTabs;
import top.ribs.scguns.item.GunItem;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.registries.ModItems.*;
import static top.ribs.scguns.init.ModItems.*;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CreativeTabEvent {
    //The reason why we're doing this here instead of Blueprint's CreativeModeTabContentsPopulator is because blueprint's doesn't properly get the max ammo of guns
    //So they're all added with 30 current ammo, regardless of the max ammo
    //I'm still using the other one because for the base items it works fine.
    @SubscribeEvent
    public static void populateTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ModCreativeModeTabs.SCORCHED_GUNS_TAB.getKey()) {
            MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entry = event.getEntries();
            putGunItemsAfterGun(entry, SHARD_CULLER, MORTICIAN, BELLA, REHEARSE, ANATHEMA, HANGMAN_CARBINE, GALLOWS, CACOPHONY, KETERIYA, NECROSIS, SILVER_LINING, RIBCAGE);
            putGunItemsAfterGun(entry, DOZIER_RL, LUSTRE, SCATTERER, ELECTROTHERMAL_AUTOCANNON);
            putGunItemsAfterGun(entry, PRIMA_MATERIA, CHARYBDIS);
            putGunItemsAfterGun(entry, GRANDLE, RASCAL);
            putGunItemsAfterGun(entry, IRON_SPEAR, IRON_PARTISAN);
            putGunItemsAfterGun(entry, PRUSH_GUN, RECUR);
        }
    }

    @SafeVarargs
    public static void putGunItemsAfterGun(MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entry, RegistryObject<? extends Item> before, RegistryObject<? extends Item>... stack) {
        for (RegistryObject<? extends Item> registryObject : stack) {
            entry.putAfter(getGunWithFullAmmo(before), getGunWithFullAmmo(registryObject), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    private static ItemStack getGunWithFullAmmo(RegistryObject<? extends Item> item) {
        ItemStack stack = item.get().getDefaultInstance();
        Item _item = item.get();
        GunItem gunItem = (GunItem) _item;
        if (_item instanceof RechargeableEnergyGunItem energyGunItem) {
            stack.getOrCreateTag().putInt("Energy", energyGunItem.getMaxEnergyStored(stack));
        }
        stack.getOrCreateTag().putInt("AmmoCount", gunItem.getGun().getReloads().getMaxAmmo());

        return stack;
    }
}
