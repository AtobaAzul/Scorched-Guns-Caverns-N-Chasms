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
        public final ForgeConfigSpec.DoubleValue magic_damage_percent;
        public final ForgeConfigSpec.IntValue required_raid_tier;
        public final ForgeConfigSpec.IntValue lustre_energy_cost;
        public final ForgeConfigSpec.IntValue lustre_refill_cd;
        public final ForgeConfigSpec.IntValue lustre_energy_max;
        public final ForgeConfigSpec.DoubleValue lustre_recharge_mult;
        public final ForgeConfigSpec.IntValue electrothermal_autocannon_energy_cost;
        public final ForgeConfigSpec.IntValue electrothermal_autocannon_energy_max;
        public final ForgeConfigSpec.IntValue electrothermal_autocannon_refill_cd;
        public final ForgeConfigSpec.DoubleValue electrothermal_autocannon_recharge_mult;
        public final ForgeConfigSpec.IntValue scatterer_energy_cost;
        public final ForgeConfigSpec.IntValue scatterer_refill_cd;
        public final ForgeConfigSpec.IntValue scatterer_energy_max;
        public final ForgeConfigSpec.DoubleValue scatterer_recharge_mult;
        public final ForgeConfigSpec.IntValue gun_melee_magic_damage;
        public final ForgeConfigSpec.IntValue anathema_reload_amount;
        public final ForgeConfigSpec.DoubleValue ricoshot_crit_chance;
        public final ForgeConfigSpec.DoubleValue ricoshot_crit_damage;
        public final ForgeConfigSpec.DoubleValue bluntshot_knockback;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Items & Guns").push("items");
            {
                this.magic_damage_percent = builder.comment("The percentage of magic damage dealt by Hex Rounds").defineInRange("magic_damage_percent", 0.5, 0.0, 1.0);
                this.gun_melee_magic_damage = builder.comment("Magic damage dealt by Keteriya/Hangman Acolyte melee").defineInRange("gun_melee_magic_damage", 4, 0, Integer.MAX_VALUE);
                this.anathema_reload_amount = builder.comment("Amount of ammo restored by melee with the Anathema").defineInRange("anathema_reload_amount", 3, 1, Integer.MAX_VALUE);
                this.ricoshot_crit_chance = builder.comment("The crit chance increase per RicoShot ricochet").defineInRange("ricoshot_crit_chance", 0.5, 0, Float.MAX_VALUE);
                this.ricoshot_crit_damage = builder.comment("The crit damage increase per RicoShot ricochet").defineInRange("ricoshot_crit_damage", 0.5, 0, Float.MAX_VALUE);
                this.bluntshot_knockback = builder.comment("The amount of extra knockpack bluntshot applies on hit").defineInRange("bluntshot_knockback", 4.4f, 0f, Float.MAX_VALUE);

                builder.push("energy guns");
                builder.comment("Lustre");
                this.lustre_energy_cost = builder.comment("The amount of FE energy used to add 1 ammo to the magazine").defineInRange("lustre_energy_cost", 10000, 1, Integer.MAX_VALUE);
                this.lustre_energy_max = builder.comment("The amount of FE energy the gun can store").defineInRange("lustre_energy_max", 300000, 1, Integer.MAX_VALUE);
                this.lustre_refill_cd = builder.comment("The amount of time, in ticks, for the gun to add 1 ammo to the magazine.").defineInRange("lustre_refill_cd", 40, 0, Integer.MAX_VALUE);
                this.lustre_recharge_mult = builder.comment("A multiplier of how much longer it takes to add 1 ammo to the gun's magazine after shooting.").defineInRange("lustre_recharge_mult", 2f, 1f, Float.MAX_VALUE);

                builder.comment("Electrothermal Autocannon");
                this.electrothermal_autocannon_energy_cost = builder.comment("The amount of FE energy used to add 1 ammo to the magazine").defineInRange("electrothermal_autocannon_energy_cost", 750, 1, Integer.MAX_VALUE);
                this.electrothermal_autocannon_energy_max = builder.comment("The amount of FE energy the gun can store").defineInRange("electrothermal_autocannon_energy_max", 300000, 1, Integer.MAX_VALUE);
                this.electrothermal_autocannon_refill_cd = builder.comment("The amount of time, in ticks, for the gun to add 1 ammo to the magazine.").defineInRange("electrothermal_autocannon_refill_cd", 2, 0, Integer.MAX_VALUE);
                this.electrothermal_autocannon_recharge_mult = builder.comment("A multiplier of how much longer it takes to add 1 ammo to the gun's magazine after shooting.").defineInRange("electrothermal_autocannon_recharge_mult", 50f, 1f, Float.MAX_VALUE);

                builder.comment("Scatterer");
                this.scatterer_energy_cost = builder.comment("The amount of FE energy used to add 1 ammo to the magazine").defineInRange("scatterer_energy_cost", 1000, 1, Integer.MAX_VALUE);
                this.scatterer_energy_max = builder.comment("The amount of FE energy the gun can store").defineInRange("scatterer_energy_max", 144000, 1, Integer.MAX_VALUE);
                this.scatterer_refill_cd = builder.comment("The amount of time, in ticks, for the gun to add 1 ammo to the magazine.").defineInRange("scatterer_refill_cd", 10, 0, Integer.MAX_VALUE);
                this.scatterer_recharge_mult = builder.comment("A multiplier of how much longer it takes to add 1 ammo to the gun's magazine after shooting.").defineInRange("scatterer_recharge_mult", 10f, 1f, Float.MAX_VALUE);
            }
            builder.pop();

            builder.comment("Entities").push("entities");
            {
                this.required_raid_tier = builder.comment("The minimum required unlocked \"raid tier\" for Gravekeeper Schisms and Acolytes to spawn. If 0, ignores raid tier entirely.\nFor reference, use the command /scguns progression check <playername>").defineInRange("required_raid_tier", 4, 0, Integer.MAX_VALUE);
            }
            builder.pop();
        }
    }

    public static class Client {
        public Client(ForgeConfigSpec.Builder builder) {

        }
    }
}
