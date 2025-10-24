package net.atobaazul.scguns_cnc.registries;

import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlockSubRegistryHelper extends BlockSubRegistryHelper {
    public ModBlockSubRegistryHelper(RegistryHelper parent) {
        super(parent, parent.getItemSubHelper().getDeferredRegister(), parent.getBlockSubHelper().getDeferredRegister());
    }

    public <B extends Block> RegistryObject<B> createPlacedItem(String name, Supplier<? extends B> supplier) {
        RegistryObject<B> block = this.deferredRegister.register(name, supplier);
        this.itemRegister.register(name + "_placed", () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
}
