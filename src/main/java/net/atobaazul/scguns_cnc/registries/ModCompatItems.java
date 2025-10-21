package net.atobaazul.scguns_cnc.registries;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import net.minecraft.world.item.Item;

public class ModCompatItems {
    public static Item getSequencedItem(String name) {
        return new SequencedAssemblyItem(new Item.Properties());
    }
}
