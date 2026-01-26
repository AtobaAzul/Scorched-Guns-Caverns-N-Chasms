package net.atobaazul.scguns_cnc.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class ModTags {
    public static final TagKey<Item> GRAVEKEEPER_GUN_TIER = createSCGunsItemTag("gravekeeper_gun_tier");
    public static final TagKey<Item> VAULT_GUN_TIER = createSCGunsItemTag("vault_gun_tier");
    public static final TagKey<Item> HEX_BULLET_TIP = createItemTag("hex_bullet_tip");
    public static final TagKey<Item> INCREASED_CASING_DROP_CHANCE = createItemTag("increased_casing_drop_chance");

    private static TagKey<Block> createBlockTag(String tagName) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, tagName));
    }

    private static TagKey<Item> createItemTag(String tagName) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, tagName));
    }

    private static TagKey<Item> createSCGunsItemTag(String tagName) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("scguns", tagName));
    }
}
