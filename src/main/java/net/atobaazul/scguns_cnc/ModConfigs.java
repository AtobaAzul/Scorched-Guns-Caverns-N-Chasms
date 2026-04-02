package net.atobaazul.scguns_cnc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ModConfigs {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();

        Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static class Common {
        Common(ForgeConfigSpec.Builder builder) {

        }
    }

    public static class Client {
        public Client(ForgeConfigSpec.Builder builder) {

        }
    }
}
