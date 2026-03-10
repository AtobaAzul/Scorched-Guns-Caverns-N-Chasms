package net.atobaazul.scguns_cnc.datagen.providers.server;

import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import top.ribs.scguns.init.ModTags;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class ModBlockTagProvider extends BlockTagsProvider {
    ArrayList<RegistryObject<Block>> FRAGILE_BLOCKS = new ArrayList<>();

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        FRAGILE_BLOCKS.add(CCBlocks.CUPRIC_LANTERN);
        FRAGILE_BLOCKS.add(CCBlocks.COPPER_LANTERN);
        FRAGILE_BLOCKS.add(CCBlocks.EXPOSED_COPPER_LANTERN);
        FRAGILE_BLOCKS.add(CCBlocks.WEATHERED_COPPER_LANTERN);
        FRAGILE_BLOCKS.add(CCBlocks.OXIDIZED_COPPER_LANTERN);
        FRAGILE_BLOCKS.add(CCBlocks.WAXED_COPPER_LANTERN);
        FRAGILE_BLOCKS.add(CCBlocks.WAXED_EXPOSED_COPPER_LANTERN);
        FRAGILE_BLOCKS.add(CCBlocks.WAXED_WEATHERED_COPPER_LANTERN);
        FRAGILE_BLOCKS.add(CCBlocks.WAXED_OXIDIZED_COPPER_LANTERN);

        FRAGILE_BLOCKS.forEach(this::addFragileBlock);
    }

    private void addFragileBlock(RegistryObject<Block> block) {
        this.tag(ModTags.Blocks.FRAGILE).add(block.get());
    }
}
