package net.atobaazul.scguns_cnc.datagen.providers.client;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.registries.ModItems.*;


public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(HEX_ROUND);
        simpleItem(COMPACT_HEX_ROUND);
        simpleItem(GRAVEKEEPER_BLUEPRINT);
        simpleItem(NECROMIUM_GUN_FRAME);
        simpleItem(BLUNTSHOT);
        simpleItem(SMALL_NECROMIUM_CASING);
        simpleItem(MEDIUM_NECROMIUM_CASING);
        simpleItem(SILVER_BULLET);
        simpleItem(THE_HUNGER);
        simpleItem(COPPER_SLUG);
        simpleItem(HEXSHOT);
        simpleItem(HEX_BUCKSHOT);

        simpleItem(UNFINISHED_HEX_ROUND);
        simpleItem(UNFINISHED_COMPACT_HEX_ROUND);
        simpleItem(UNFINISHED_HEXSHOT);
        simpleItem(UNFINISHED_COPPER_SLUG);

        simpleItem(LESSER_STRAWMAN);
        simpleItem(GRAVEKEEPER_FLARE);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(MOD_ID, "item/" + item.getId().getPath()));
    }
}
