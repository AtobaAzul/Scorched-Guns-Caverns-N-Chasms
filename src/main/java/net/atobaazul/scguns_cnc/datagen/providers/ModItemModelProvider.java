package net.atobaazul.scguns_cnc.datagen.providers;

import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;


public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.HEX_ROUND);
        simpleItem(ModItems.COMPACT_HEX_ROUND);
        simpleItem(ModItems.GRAVEKEEPER_BLUEPRINT);
        simpleItem(ModItems.NECROMIUM_GUN_FRAME);
        simpleItem(ModItems.BLUNTSHOT);
        simpleItem(ModItems.SMALL_NECROMIUM_CASING);
        simpleItem(ModItems.MEDIUM_NECROMIUM_CASING);
        simpleItem(ModItems.SILVER_BULLET);
        simpleItem(ModItems.THE_HUNGER);

        simpleItem(ModItems.UNFINISHED_HEX_ROUND);
        simpleItem(ModItems.UNFINISHED_COMPACT_HEX_ROUND);

        simpleItem(ModItems.LESSER_STRAWMAN);
        simpleItem(ModItems.GRAVEKEEPER_FLARE);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(MOD_ID, "item/" + item.getId().getPath()));
    }
}
