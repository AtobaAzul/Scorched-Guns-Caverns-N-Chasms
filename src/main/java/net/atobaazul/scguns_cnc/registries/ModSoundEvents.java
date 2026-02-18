package net.atobaazul.scguns_cnc.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);

    public static final RegistryObject<SoundEvent> HANGMAN_FIRE = register("item.hangman.fire");
    public static final RegistryObject<SoundEvent> HANGMAN_SILENCED_FIRE = register("item.hangman.silended_fire");

    public static final RegistryObject<SoundEvent> RIBCARE_FIRE = register("item.ribcage.fire");
    public static final RegistryObject<SoundEvent> BELLA_FIRE = register("item.bella.fire");
    public static final RegistryObject<SoundEvent> BELLA_SILENCED_FIRE = register("item.bella.silenced_fire");

    public static final RegistryObject<SoundEvent> REHEARSE_FIRE = register("item.rehearse.fire");
    public static final RegistryObject<SoundEvent> REHEARSE_SILENCED_FIRE = register("item.rehearse.silenced_fire");
    public static final RegistryObject<SoundEvent> ANATHEMA_FIRE = register("item.anathema.fire");
    public static final RegistryObject<SoundEvent> NECROSIS_FIRE = register("item.necrosis.fire");
    public static final RegistryObject<SoundEvent> KETERIYA_FIRE = register("item.keteriya.fire");
    public static final RegistryObject<SoundEvent> GALLOWS_FIRE = register("item.gallows.fire");
    public static final RegistryObject<SoundEvent> GALLOWS_SILENCED_FIRE = register("item.gallows.silenced_fire");

    public static final RegistryObject<SoundEvent> ELECTROTHERMAL_AUTOCANNON_FIRE = register("item.electrothermal_autocannon.fire");
    public static final RegistryObject<SoundEvent> ELC_FIRE = register("item.elc.fire");

    public static final RegistryObject<SoundEvent> LUSTRE_FIRE = register("item.lustre.fire");
    public static final RegistryObject<SoundEvent> LUSTRE_JAM = register("item.lustre.jam");
    public static final RegistryObject<SoundEvent> LUSTRE_MAG_IN = register("item.lustre.mag_in");
    public static final RegistryObject<SoundEvent> LUSTRE_MAG_OUT = register("item.lustre.mag_out");

    public static final RegistryObject<SoundEvent> SILVERLINING_FIRE = register("item.silverlining.fire");
    public static final RegistryObject<SoundEvent> SILVERLINING_PULL_START = register("item.silverlining.pull1");
    public static final RegistryObject<SoundEvent> SILVERLINING_PULL_MIDDLE = register("item.silverlining.pull2");
    public static final RegistryObject<SoundEvent> SILVERLINING_PULL_END = register("item.silverlining.pull3");

    public static final RegistryObject<SoundEvent> MORTICIAN_FIRE = register("item.mortician.fire");
    public static final RegistryObject<SoundEvent> CACOPHONY_FIRE = register("item.cacophony.fire");

    public static final RegistryObject<SoundEvent> MALISON_EXPLOSION = register("item.malison_grenade.explode");

    public static final RegistryObject<SoundEvent> SILVER_FLYBY = register("item.flyby.silver_flyby");

    private static RegistryObject<SoundEvent> register(String key) {
        return REGISTER.register(key, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, key)));
    }
}
