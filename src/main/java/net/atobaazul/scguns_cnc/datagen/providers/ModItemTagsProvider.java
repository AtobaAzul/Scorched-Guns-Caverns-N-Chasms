package net.atobaazul.scguns_cnc.datagen.providers;


import net.atobaazul.scguns_cnc.registries.ModItems;
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


public class ModItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider {
    public ModItemTagsProvider(@NotNull PackOutput output, @NotNull CompletableFuture<HolderLookup.Provider> lookupProvider, @NotNull CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, MOD_ID, existingFileHelper);
    }

    //damnit ribs
    private static final TagKey<Item>  PISTOL_AMMO = ItemTags.create(new ResourceLocation("scguns", "pistol_ammo"));
    private static final TagKey<Item>  RIFLE_AMMO = ItemTags.create(new ResourceLocation("scguns", "rifle_ammo"));
    private static final TagKey<Item>  SHOTGUN_AMMO = ItemTags.create(new ResourceLocation("scguns", "shotgun_ammo"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.ONE_HANDED_CARBINE)
                .add(ModItems.REHEARSE.get());

        tag(PISTOL_AMMO)
                .add(ModItems.COMPACT_HEX_ROUND.get());

        tag(RIFLE_AMMO)
                .add(ModItems.HEX_ROUND.get());

        tag(SHOTGUN_AMMO)
                .add(ModItems.BLUNTSHOT.get());

    }
}
