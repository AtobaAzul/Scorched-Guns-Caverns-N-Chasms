package net.atobaazul.scguns_cnc;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;

public class CompatManager {
    public static final boolean CREATE_ENABLED = getModEnabled("create");

    private static boolean getModEnabled(String name) {
        return FMLLoader.getLoadingModList().getModFileById(name) != null;
    }
}
