package net.atobaazul.scguns_cnc.registries;

import com.teamabnormals.caverns_and_chasms.common.block.IngotBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import top.ribs.scguns.init.ModItems;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.SCGunsCnC.REGISTRY_HELPER;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {
    public static final ModBlockSubRegistryHelper HELPER = REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<Block> SCORCHED_INGOT = createSimpleIngotBlock("scorched", ModItems.SCORCHED_INGOT);
    public static final RegistryObject<Block> ANTHRALITE_INGOT = createSimpleIngotBlock("anthralite", ModItems.ANTHRALITE_INGOT);
    public static final RegistryObject<Block> TREATED_IRON_INGOT = createSimpleIngotBlock("treated_iron", ModItems.TREATED_IRON_INGOT);
    public static final RegistryObject<Block> TREATED_BRASS_INGOT = createSimpleIngotBlock("treated_brass", ModItems.TREATED_BRASS_INGOT);
    public static final RegistryObject<Block> DEPLETED_DIAMOND_STEEL_INGOT = createSimpleIngotBlock("depleted_diamond_steel", ModItems.DEPLETED_DIAMOND_STEEL_INGOT);
    public static final RegistryObject<Block> DIAMOND_STEEL_INGOT = createSimpleIngotBlock("diamond_steel", ModItems.DIAMOND_STEEL_INGOT);

    private static RegistryObject<Block> createSimpleIngotBlock(String name, RegistryObject<Item> item) {

        return HELPER.createPlacedItem(name + "_ingot", () -> new IngotBlock(() -> item.get(), BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    }

    private static RegistryObject<Block> createSimpleIngotBlock(String name, Item item, Block block) {
        return HELPER.createPlacedItem(name + "_ingot", () -> new IngotBlock(() -> item, BlockBehaviour.Properties.copy(block)));
    }
}
