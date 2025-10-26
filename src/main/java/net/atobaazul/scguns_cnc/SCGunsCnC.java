package net.atobaazul.scguns_cnc;

import com.mojang.logging.LogUtils;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.atobaazul.scguns_cnc.client.CCClientHandler;
import net.atobaazul.scguns_cnc.common.entity.*;
import net.atobaazul.scguns_cnc.events.client.Particles;
import net.atobaazul.scguns_cnc.registries.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import top.ribs.scguns.common.ProjectileManager;

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

        ModItems.REGISTER.register(modEventBus);
        ModEntities.REGISTER.register(modEventBus);
        Particles.REGISTER.register(modEventBus);
        ModSoundEvents.REGISTER.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            CCClientHandler.registerClientHandlers(modEventBus);
            BlueprintScreen.registerLoreOnlyItem(new ResourceLocation(MOD_ID, "gravekeeper_blueprint"), "anathema");
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        GunTierRegistry.register("gravekeeper", 6, "gravekeeper_gun_tier", 4);

        ProjectileManager.getInstance().registerFactory(ModItems.COMPACT_HEX_ROUND.get(), (worldIn, entity, weapon, item, modifiedGun) -> new HexRoundProjectileEntity(ModEntities.HEX_ROUND_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.HEX_ROUND.get(), (worldIn, entity, weapon, item, modifiedGun) -> new HexRoundProjectileEntity(ModEntities.HEX_ROUND_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.BLUNTSHOT.get(), (worldIn, entity, weapon, item, modifiedGun) -> new BluntshotProjectileEntity(ModEntities.BLUNTSHOT.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(CCItems.LARGE_ARROW.get(), (worldIn, entity, weapon, item, modifiedGun) -> new DummyProjectileEntity(ModEntities.DUMMY_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.THE_HUNGER.get(), (worldIn, entity, weapon, item, modifiedGun) -> new BloodShotProjectileEntity(ModEntities.BLOOD_SHOT.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.STRIKER_ROUND.get(), (worldIn, entity, weapon, item, modifiedGun) -> new StrikerRoundProjectileEntity(ModEntities.STRIKER_ROUND_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));

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
