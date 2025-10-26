package net.atobaazul.scguns_cnc.registries;

import net.minecraft.resources.ResourceLocation;
import top.ribs.scguns.common.FireMode;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

public class CCFireModes {
    public static final FireMode BURST_BEAM = new FireMode(new ResourceLocation(MOD_ID, "burst_beam"));

    public void init() {
        FireMode.registerType(BURST_BEAM);
    }
}
