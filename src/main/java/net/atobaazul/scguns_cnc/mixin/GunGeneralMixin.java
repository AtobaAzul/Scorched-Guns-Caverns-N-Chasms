package net.atobaazul.scguns_cnc.mixin;

import net.atobaazul.scguns_cnc.util.GunGeneralExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.ribs.scguns.common.Gun;

@Mixin(targets = "top.ribs.scguns.common.Gun$General")
public class GunGeneralMixin implements GunGeneralExtension {


    @Shadow private boolean allowAmmoChange;

    @Override
    public void setAmmoChange(boolean val) {
        this.allowAmmoChange = val;
    }
}
