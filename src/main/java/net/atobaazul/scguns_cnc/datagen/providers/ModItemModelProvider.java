package net.atobaazul.scguns_cnc.datagen.providers;

import net.atobaazul.scguns_cnc.registries.Items;
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
        simpleItem(Items.HEX_ROUND);
        simpleItem(Items.COMPACT_HEX_ROUND);
        simpleItem(Items.GRAVEKEEPER_BLUEPRINT);
        simpleItem(Items.NECROMIUM_GUN_FRAME);
        simpleItem(Items.BLUNTSHOT);
        simpleItem(Items.SMALL_NECROMIUM_CASING);
        simpleItem(Items.MEDIUM_NECROMIUM_CASING);
        simpleItem(Items.SILVER_BULLET);
        simpleItem(Items.THE_HUNGER);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(MOD_ID, "item/" + item.getId().getPath()));
    }
}
