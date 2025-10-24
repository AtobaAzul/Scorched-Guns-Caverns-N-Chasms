package net.atobaazul.scguns_cnc.datagen.providers.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.registries.ModBlocks.*;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ANTHRALITE_INGOT.get())
                .add(SCORCHED_INGOT.get())
                .add(TREATED_BRASS_INGOT.get())
                .add(TREATED_IRON_INGOT.get())
                .add(DEPLETED_DIAMOND_STEEL_INGOT.get())
                .add(DIAMOND_STEEL_INGOT.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ANTHRALITE_INGOT.get())
                .add(SCORCHED_INGOT.get())
                .add(TREATED_BRASS_INGOT.get())
                .add(TREATED_IRON_INGOT.get())
                .add(DEPLETED_DIAMOND_STEEL_INGOT.get())
                .add(DIAMOND_STEEL_INGOT.get());

    }
}
