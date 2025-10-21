package net.atobaazul.scguns_cnc.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.atobaazul.scguns_cnc.registries.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JEICCPlugin implements IModPlugin {
    public static MutableComponent getTranslation(String key, Object... args) {
        return Component.translatable("scguns_cnc." + key, args);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addIngredientInfo(new ItemStack(ModItems.GRAVEKEEPER_BLUEPRINT.get()), VanillaTypes.ITEM_STACK, getTranslation("jei.info.gravekeeper_blueprint"));
        registration.addIngredientInfo(new ItemStack(ModItems.ANATHEMA.get()), VanillaTypes.ITEM_STACK, getTranslation("jei.info.gravekeeper_blueprint"));
    }
}
