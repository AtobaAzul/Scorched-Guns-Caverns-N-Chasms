package net.atobaazul.scguns_cnc.mixin;

import com.google.gson.JsonObject;
import net.atobaazul.scguns_cnc.util.GunGeneralExtension;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.ribs.scguns.Reference;
import top.ribs.scguns.ScorchedGuns;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.init.ModItems;

import java.util.List;

@Mixin(Gun.class)
public class GunMixin {
    //I don't want to overwrite the whole method but i'm tired.
    //TODO: Don't do that.

    @Shadow
    protected List<Gun.Projectile> alternateProjectiles;

    @Shadow
    protected Gun.General general;

    @Shadow
    protected Gun.Projectile projectile;
    boolean addSlug = false;
    boolean addRicoShot = false;

    @Inject(method = "loadAlternateProjectilesFromJson", at = @At("HEAD"), remap = false)
    public void scguns_cnc$loadAlternateProjectilesFromJson(JsonObject jsonObject, CallbackInfo ci) {
        this.alternateProjectiles.clear();

        GunGeneralExtension eGeneral = (GunGeneralExtension) this.general;

        eGeneral.setAmmoChange(true);

        if (this.general.allowsAmmoChange()) {
            this.general.getAvailableAmmoTypes().clear();

            if (this.projectile.item != null) {
                this.general.getAvailableAmmoTypes().add(this.projectile.item.toString());
            }
        }
        
        int index = 1;
        while (jsonObject.has("projectile_" + index)) {
            try {
                JsonObject projJson = jsonObject.getAsJsonObject("projectile_" + index);
                Gun.Projectile altProjectile = new Gun.Projectile();

                CompoundTag nbt = new CompoundTag();
                projJson.entrySet().forEach(entry -> {
                    String jsonKey = entry.getKey();
                    String nbtKey = Character.toUpperCase(jsonKey.charAt(0)) + jsonKey.substring(1);
                    com.google.gson.JsonElement value = entry.getValue();
                    if (value.isJsonPrimitive()) {
                        com.google.gson.JsonPrimitive prim = value.getAsJsonPrimitive();
                        if (prim.isString()) {
                            String stringValue = prim.getAsString();
                            if (jsonKey.equals("item") && !stringValue.contains(":")) {
                                stringValue = Reference.MOD_ID + ":" + stringValue;
                            }
                            if (jsonKey.equals("item")) {
                                System.out.println(ModItems.BEARPACK_SHELL.get());
                                System.out.println(stringValue);
                                if (stringValue.equals("scguns:" + ModItems.BEARPACK_SHELL.get().toString()) && !addSlug) {
                                    System.out.println("HELLO ADDING SLUG");
                                    addSlug = true;
                                }
                            }

                            nbt.putString(nbtKey, stringValue);
                        } else if (prim.isNumber()) {
                            if (prim.getAsString().contains(".")) {
                                nbt.putDouble(nbtKey, prim.getAsDouble());
                            } else {
                                nbt.putInt(nbtKey, prim.getAsInt());
                            }
                        } else if (prim.isBoolean()) {
                            nbt.putBoolean(nbtKey, prim.getAsBoolean());
                        }
                    }
                });
                altProjectile.deserializeNBT(nbt);
                this.alternateProjectiles.add(altProjectile);

                if (addSlug) {
                    //private :(
                    //this.general.allowAmmoChange = true;
                    Gun.Projectile slugProjectile = altProjectile.copy();
                    CompoundTag slugNBT = slugProjectile.serializeNBT();
                    slugNBT.putFloat("Damage", slugNBT.getFloat("Damage") * 0.7f);
                    slugNBT.putBoolean("Gravity", true);
                    slugNBT.putString("CasingType", "scguns:large_brass_casing");
                    slugNBT.putString("CasingParticle", "scguns:brass_casing");
                    slugNBT.putString("Item", "scguns_cnc:copper_slug");

                    slugProjectile.deserializeNBT(slugNBT);
                    this.alternateProjectiles.add(slugProjectile);
                    if (this.general.allowsAmmoChange()) {
                        if (slugProjectile.item != null && addSlug) {
                            this.general.getAvailableAmmoTypes().add(slugProjectile.item.toString());
                        }
                    }
                }

                if (this.general.allowsAmmoChange()) {
                    if (altProjectile.item != null) {
                        this.general.getAvailableAmmoTypes().add(altProjectile.item.toString());
                    }

                }
                index++;
            } catch (Exception e) {
                ScorchedGuns.LOGGER.error("(Modified from Scguns:CnC) Failed to load projectile_{}: {}", index, e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
}
