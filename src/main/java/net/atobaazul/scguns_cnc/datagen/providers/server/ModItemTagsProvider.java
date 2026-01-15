package net.atobaazul.scguns_cnc.datagen.providers.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import top.ribs.scguns.init.ModTags;

import java.util.concurrent.CompletableFuture;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.common.ModTags.*;
import static net.atobaazul.scguns_cnc.registries.ModItems.*;


public class ModItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider {
    //damnit ribs
    private static final TagKey<Item> PISTOL_AMMO = ItemTags.create(new ResourceLocation("scguns", "pistol_ammo"));
    private static final TagKey<Item> RIFLE_AMMO = ItemTags.create(new ResourceLocation("scguns", "rifle_ammo"));
    private static final TagKey<Item> SHOTGUN_AMMO = ItemTags.create(new ResourceLocation("scguns", "shotgun_ammo"));
    private static final TagKey<Item> AMMO = ItemTags.create(new ResourceLocation("scguns", "ammo"));
    private static final TagKey<Item> EMPTY_CASING = ItemTags.create(new ResourceLocation("scguns", "empty_casing"));
    private static final TagKey<Item> SILVER_NUGGETS = ItemTags.create(new ResourceLocation("forge", "nuggets/silver"));

    public ModItemTagsProvider(@NotNull PackOutput output, @NotNull CompletableFuture<HolderLookup.Provider> lookupProvider, @NotNull CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, MOD_ID, existingFileHelper);
    }


    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.ONE_HANDED_CARBINE).add(REHEARSE.get());

        tag(EMPTY_CASING)
            .add(SMALL_NECROMIUM_CASING.get())
            .add(MEDIUM_NECROMIUM_CASING.get());

        tag(AMMO)
            .add(COMPACT_HEX_ROUND.get())
            .add(HEX_ROUND.get())
            .add(BLUNTSHOT.get())
            .add(COPPER_SLUG.get())
            .add(HEXSHOT.get())
            .add(PULSE_CORE.get());

        tag(INCREASED_CASING_DROP_CHANCE)
            .add(SMALL_NECROMIUM_CASING.get())
            .add(MEDIUM_NECROMIUM_CASING.get());


        tag(PISTOL_AMMO)
            .add(COMPACT_HEX_ROUND.get());

        tag(RIFLE_AMMO)
            .add(HEX_ROUND.get());

        tag(SHOTGUN_AMMO)
            .add(BLUNTSHOT.get())
            .add(COPPER_SLUG.get())
            .add(HEXSHOT.get());

        tag(GRAVEKEEPER_GUN_TIER)
            .add(HANGMAN_CARBINE.get())
            .add(RIBCAGE.get())
            .add(BELLA.get())
            .add(REHEARSE.get())
            .add(SILVER_LINING.get())
            .add(KETERIYA.get())
            .add(ANATHEMA.get())
            .add(GALLOWS.get())
            .add(NECROSIS.get());

        tag(HEX_BULLET_TIP)
             .add(SILVER_BULLET.get())
             .addOptionalTag(SILVER_NUGGETS);

        tag(VAULT_GUN_TIER)
                .add(LUSTRE.get())
                .add(ELECTROLASER_AUTOCANNON.get());

        tag(ModTags.Items.HEAVY_WEAPON).add(LUSTRE.get())
                .add(ELECTROLASER_AUTOCANNON.get());
    }
}
