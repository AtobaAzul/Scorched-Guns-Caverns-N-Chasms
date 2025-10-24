package net.atobaazul.scguns_cnc.datagen.providers.server;

import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import com.teamabnormals.caverns_and_chasms.common.block.IngotBlock;
import com.teamabnormals.caverns_and_chasms.common.block.IngotLayer;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.registries.ModBlocks.*;

public class ModBlockStateProvider extends BlueprintBlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.ingotBlock(SCORCHED_INGOT);
        this.ingotBlock(TREATED_BRASS_INGOT);
        this.ingotBlock(TREATED_IRON_INGOT);
        this.ingotBlock(DEPLETED_DIAMOND_STEEL_INGOT);
        this.ingotBlock(DIAMOND_STEEL_INGOT);
        this.ingotBlock(ANTHRALITE_INGOT);
    }

    public void ingotBlock(RegistryObject<Block> registryObject) {
        Block block = registryObject.get();

        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block);
        this.addIngotLayer(builder, block, 1, 1, 2, 3);
        this.addIngotLayer(builder, block, 2, 2, 3);
        this.addIngotLayer(builder, block, 3, 3);
        this.addIngotLayer(builder, block, 4);

        this.placedItemModel(block);
    }

    public void placedItemModel(Block block) {
        this.itemModels().withExistingParent(ForgeRegistries.BLOCKS.getKey(block).withSuffix("_placed").getPath(), "item/generated").texture("layer0", ForgeRegistries.ITEMS.getKey(Items.BARRIER).withPrefix("item/"));
    }

    public void addIngotLayer(MultiPartBlockStateBuilder builder, Block block, int i, Integer... nums) {
        this.addIngotModel(builder, block, IngotLayer.LEFT, Direction.Axis.X, i, nums);
        this.addIngotModel(builder, block, IngotLayer.RIGHT, Direction.Axis.X, i, nums);
        this.addIngotModel(builder, block, IngotLayer.LEFT, Direction.Axis.Z, i, nums);
        this.addIngotModel(builder, block, IngotLayer.RIGHT, Direction.Axis.Z, i, nums);
    }



    public void addIngotModel(MultiPartBlockStateBuilder builder, Block block, IngotLayer ingotLayer, Direction.Axis axis, int layer, Integer... nums) {
        Direction.Axis visualAxis = IngotBlock.getAxisForLayer(layer, axis);
        String name = "_" + ingotLayer.getSerializedName() + "_" + visualAxis.getSerializedName() + "_layer" + layer;
        BlockModelBuilder model = models().withExistingParent(name(block) + name, new ResourceLocation(MOD_ID, "block/template_ingot" + name)).texture("ingot", blockTexture(block).toString().replace("waxed_", ""));

        if (nums.length > 0) {
            builder.part().modelFile(model).addModel().useOr().nestedGroup().condition(IngotBlock.AXIS, axis).condition(IngotBlock.LAYERS, layer - 1).condition(IngotBlock.TOP_INGOT, ingotLayer, IngotLayer.BOTH).end().nestedGroup().condition(IngotBlock.AXIS, axis).condition(IngotBlock.LAYERS, nums).end();
        } else {
            builder.part().modelFile(model).addModel().condition(IngotBlock.AXIS, axis).condition(IngotBlock.LAYERS, layer - 1).condition(IngotBlock.TOP_INGOT, ingotLayer, IngotLayer.BOTH);
        }
    }

    @Override
    public void generatedItem(ItemLike item, ItemLike texture, String type) {
        this.generatedItem(item, remove(prefix(type + "/", BlueprintItemModelProvider.key(texture)), "waxed_"));
    }

    @Override
    public ResourceLocation blockTexture(Block block) {
        ResourceLocation name = remove(ForgeRegistries.BLOCKS.getKey(block), "waxed_");
        return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
    }

    @Override
    public void blockItem(Block block) {
        this.simpleBlockItem(block, new ModelFile.ExistingModelFile(blockModel(block), this.models().existingFileHelper));
    }
    public ResourceLocation blockModel(Block block) {
        ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
        return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
    }


}
