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
    public static final RegistryObject<SoundEvent> RIBCARE_FIRE = register("item.ribcage.fire");
    public static final RegistryObject<SoundEvent> BELLA_FIRE = register("item.bella.fire");
    public static final RegistryObject<SoundEvent> REHEARSE_FIRE = register("item.rehearse.fire");

    public static final RegistryObject<SoundEvent> SILVERLINING_FIRE = register("item.silverlining.fire");
    public static final RegistryObject<SoundEvent> SILVERLINING_PULL_START = register("item.silverlining.pull1");
    public static final RegistryObject<SoundEvent> SILVERLINING_PULL_MIDDLE = register("item.silverlining.pull2");
    public static final RegistryObject<SoundEvent> SILVERLINING_PULL_END = register("item.silverlining.pull3");

    private static RegistryObject<SoundEvent> register(String key) {
        return REGISTER.register(key, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, key)));
    }
}
