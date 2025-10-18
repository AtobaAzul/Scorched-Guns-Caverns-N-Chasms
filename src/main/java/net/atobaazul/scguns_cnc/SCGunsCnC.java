package net.atobaazul.scguns_cnc;

import com.mojang.logging.LogUtils;
import net.atobaazul.scguns_cnc.common.entity.HexRoundProjectileEntity;
import net.atobaazul.scguns_cnc.events.client.Particles;
import net.atobaazul.scguns_cnc.registries.CreativeTabs;
import net.atobaazul.scguns_cnc.registries.Entities;
import net.atobaazul.scguns_cnc.registries.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.ribs.scguns.common.ProjectileManager;
import top.ribs.scguns.entity.projectile.BuckshotProjectileEntity;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.event.ModClientEventsBus;
import top.ribs.scguns.init.ModEntities;
import net.atobaazul.scguns_cnc.registries.Items;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SCGunsCnC.MOD_ID)
public class SCGunsCnC
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "scguns_cnc";
    // Directly reference a slf4j logger
    ;
    public static final Logger LOGGER = LogUtils.getLogger();

    public SCGunsCnC(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();


        modEventBus.addListener(this::commonSetup);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        Items.REGISTER.register(modEventBus);
        Entities.REGISTER.register(modEventBus);
        Particles.REGISTER.register(modEventBus);
        SoundEvents.REGISTER.register(modEventBus);
        CreativeTabs.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ProjectileManager.getInstance().registerFactory(Items.COMPACT_HEX_ROUND.get(), (worldIn, entity, weapon, item, modifiedGun) -> new HexRoundProjectileEntity(Entities.HEX_ROUND_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(Items.HEX_ROUND.get(), (worldIn, entity, weapon, item, modifiedGun) -> new HexRoundProjectileEntity(Entities.HEX_ROUND_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(Items.BLUNTSHOT.get(), (worldIn, entity, weapon, item, modifiedGun) -> new BuckshotProjectileEntity(ModEntities.BUCKSHOT_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));


    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}
