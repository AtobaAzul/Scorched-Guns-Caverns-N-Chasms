package net.atobaazul.scguns_cnc.common.item;

import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import top.ribs.scguns.entity.projectile.AdvancedRoundProjectileEntity;

import static net.atobaazul.scguns_cnc.registries.ModSoundEvents.BOOST_PACK_EQUIP;

public class BoostPackMaterial implements ArmorMaterial {
    public static final BoostPackMaterial MATERIAL = new BoostPackMaterial();

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return 1;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return 5;
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }

    @Override
    public SoundEvent getEquipSound() {
        return BOOST_PACK_EQUIP.get();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

    @Override
    public String getName() {
        return "boost_pack";
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
