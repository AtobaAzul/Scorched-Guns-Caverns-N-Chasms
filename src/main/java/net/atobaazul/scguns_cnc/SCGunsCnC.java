package net.atobaazul.scguns_cnc;

import com.mojang.logging.LogUtils;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.atobaazul.scguns_cnc.client.CCClientHandler;
import net.atobaazul.scguns_cnc.client.render.HardlightRenderLayer;
import net.atobaazul.scguns_cnc.client.render.ShieldBarRenderHandler;
import net.atobaazul.scguns_cnc.common.entity.projectile.*;
import net.atobaazul.scguns_cnc.network.C2SUseBoostMessage;
import net.atobaazul.scguns_cnc.network.PacketHandler;
import net.atobaazul.scguns_cnc.registries.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.ribs.scguns.client.screen.BlueprintScreen;
import top.ribs.scguns.common.ProjectileManager;
import top.ribs.scguns.entity.player.GunTierRegistry;

import java.util.List;

import static net.atobaazul.scguns_cnc.client.KeyBinds.BOOST_KEY;
import static net.atobaazul.scguns_cnc.registries.ModItems.BOOST_PACK;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SCGunsCnC.MOD_ID)
@Mod.EventBusSubscriber(modid = SCGunsCnC.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCGunsCnC {


    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "scguns_cnc";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public SCGunsCnC(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModItems.REGISTER.register(modEventBus);
        ModEntities.REGISTER.register(modEventBus);
        ModSoundEvents.REGISTER.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModParticleTypes.PARTICLE_TYPES.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            CCClientHandler.registerClientHandlers(modEventBus);
            BlueprintScreen.registerLoreOnlyItem(new ResourceLocation(MOD_ID, "gravekeeper_blueprint"), "anathema");
            BlueprintScreen.registerGunOrder(List.of("mortician", "bella", "rehearse", "hangman_carbine", "gallows", "cacophony", "keteriya", "necrosis", "silverlining", "ribcage", "dead_draft", "exhumer", "anathema"));
            //ModItems.setupTabEditors();

        });

        context.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT_SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        GunTierRegistry.register("gravekeeper", 6, "gravekeeper_gun_tier", 4);
        GunTierRegistry.register("vault", 6, "vault_gun_tier", 1);

        ProjectileManager.getInstance().registerFactory(ModItems.COMPACT_HEX_ROUND.get(), (worldIn, entity, weapon, item, modifiedGun) -> new HexRoundProjectileEntity(ModEntities.HEX_ROUND_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.HEX_ROUND.get(), (worldIn, entity, weapon, item, modifiedGun) -> new HexRoundProjectileEntity(ModEntities.HEX_ROUND_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.SILVER_BULLET.get(), (worldIn, entity, weapon, item, modifiedGun) -> new HexRoundProjectileEntity(ModEntities.HEX_ROUND_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.BLUNTSHOT.get(), (worldIn, entity, weapon, item, modifiedGun) -> new BluntshotProjectileEntity(ModEntities.BLUNTSHOT.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(CCItems.LARGE_ARROW.get(), (worldIn, entity, weapon, item, modifiedGun) -> new DummyProjectileEntity(ModEntities.DUMMY_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.THE_HUNGER.get(), (worldIn, entity, weapon, item, modifiedGun) -> new BloodShotProjectileEntity(ModEntities.BLOOD_SHOT.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.COPPER_SLUG.get(), (worldIn, entity, weapon, item, modifiedGun) -> new CopperSlugProjectileEntity(ModEntities.COPPER_SLUG_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.HEXSHOT.get(), (worldIn, entity, weapon, item, modifiedGun) -> new HexRoundProjectileEntity(ModEntities.HEX_ROUND_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.PULSE_CORE.get(), (worldIn, entity, weapon, item, modifiedGun) -> new EnergyBoltProjectileEntity(ModEntities.ENERGY_BOLT_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.RICOSHOT_ROUND.get(), (worldIn, entity, weapon, item, modifiedGun) -> new BouncingProjectileEntity(ModEntities.RICOSHOT_ROUND_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
        ProjectileManager.getInstance().registerFactory(ModItems.GUIDED_ROCKET.get(), (worldIn, entity, weapon, item, modifiedGun) -> new GuidedRocketProjectileEntity(ModEntities.GUIDED_ROCKET_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));

        // ProjectileManager.getInstance().registerFactory(ModItems.MALISON_GRENADE.get(), (worldIn, entity, weapon, item, modifiedGun) -> new GrenadeEntity(ModEntities.MALISON_GRENADE.get(), worldIn, entity, weapon, item, modifiedGun));
        event.enqueueWork(() -> {
            PacketHandler.init();
        });
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {


        static int particleTicks = 0;
        static int clientCooldown = 0; //Minor cooldown to let client catch up with server writing NBT;


        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {


            if (event.phase == TickEvent.Phase.END) {
                Minecraft mc = Minecraft.getInstance();
                LocalPlayer localPlayer = mc.player;
                ClientLevel level = mc.level;


                if (particleTicks > 0 && localPlayer != null & level != null) {
                    particleTicks--;

                    for (int i = 0; i < 5; i++) {
                        Vec3 dir = localPlayer.getViewVector(1.0f).normalize().scale(2);
                        Vec3 particleDir = dir.scale(-1).scale(0.25 + Mth.lerp(level.getRandom().nextFloat(), -0.1, 0.1));
                        level.addParticle(ModParticleTypes.ENERGY_BOLT_IMPACT.get(), localPlayer.getX(), localPlayer.getEyeY() - 0.5, localPlayer.getZ(), particleDir.x, particleDir.y, particleDir.z);
                    }

                    if (particleTicks < 5) {
                        for (int i = 0; i < 5; i++) {
                            Vec3 dir = localPlayer.getViewVector(1.0f).normalize().scale(2);
                            Vec3 particleDir = dir.scale(-1).scale(0.125 + Mth.lerp(level.getRandom().nextFloat(), -0.05, 0.05));
                            level.addParticle(ParticleTypes.SMOKE, localPlayer.getX(), localPlayer.getEyeY() - 0.5, localPlayer.getZ(), particleDir.x, particleDir.y, particleDir.z);
                        }
                    }
                }

                if (clientCooldown > 0) {
                    clientCooldown--;
                }

                if (BOOST_KEY.consumeClick() && localPlayer != null && level != null && !localPlayer.getCooldowns().isOnCooldown(BOOST_PACK.get())) {
                    for (ItemStack stack : localPlayer.getArmorSlots()) {
                        if (stack.is(BOOST_PACK.get()) && stack.getOrCreateTag().getBoolean("charged")) {
                            PacketHandler.getPlayChannel().sendToServer(new C2SUseBoostMessage());
                            Vec3 dir = localPlayer.getViewVector(1.0f).normalize().scale(2);
                            localPlayer.setDeltaMovement(localPlayer.getDeltaMovement().add(dir));

                            localPlayer.getCooldowns().addCooldown(BOOST_PACK.get(), ModConfigs.COMMON.boost_pack_refill_cd.get());

                            for (int i = 0; i < 20; i++) {
                                Vec3 particleDir = dir.scale(-1).scale(0.25 + Mth.lerp(level.getRandom().nextFloat(), -0.1, 0.1));
                                level.addParticle(ModParticleTypes.ENERGY_BOLT_IMPACT.get(), localPlayer.getX(), localPlayer.getEyeY() - 0.5, localPlayer.getZ(), particleDir.x, particleDir.y, particleDir.z);
                            }

                            particleTicks = 20;
                            clientCooldown = 10;
                        }
                    }
                }
            }
        }
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(ModItems::setupTabEditors);
        }

        @SubscribeEvent
        public static void clientRenderSetup(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "hardlight_shield", (gui, poseStack, partialTick, width, height) -> ShieldBarRenderHandler.renderShieldBar(gui, poseStack.pose(), width, height, poseStack));
        }

        @SubscribeEvent
        public static void addRenderLayers(EntityRenderersEvent.AddLayers event) {
            for (String skin : event.getSkins()) {
                PlayerRenderer renderer = event.getSkin(skin);
                if (renderer != null) {
                    renderer.addLayer(new HardlightRenderLayer(renderer, renderer.getModel()));
                }
            }
        }
    }
}
