package net.atobaazul.scguns_cnc.registries;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.ribs.scguns.Reference;
import top.ribs.scguns.item.AmmoItem;
import top.ribs.scguns.item.BlueprintItem;

public class Items {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<Item> GRAVEKEEPER_BLUEPRINT = REGISTER.register("gravekeeper_blueprint", () -> new BlueprintItem(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> HEX_ROUND = REGISTER.register("hex_round", () -> new AmmoItem(new Item.Properties()));
    public static final RegistryObject<Item> COMPACT_HEX_ROUND = REGISTER.register("compact_hex_round", () -> new AmmoItem(new Item.Properties()));

}
