package net.atobaazul.scguns_cnc.events.server.entity;

import net.atobaazul.scguns_cnc.common.entity.*;
import net.atobaazul.scguns_cnc.registries.ModEntities;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;
import static net.atobaazul.scguns_cnc.registries.ModEntities.GRAVEKEEPER_ACOLYTE;
import static net.atobaazul.scguns_cnc.registries.ModEntities.GRAVEKEEPER_SCHISM;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GRAVEKEEPER_GHOUL.get(), GravekeeperGhoulEntity.setAttributes());
        event.put(ModEntities.GRAVEKEEPER_NEOPHYTE.get(), GravekeeperNeophyteEntity.setAttributes());
        event.put(ModEntities.GRAVEKEEPER_HERALD.get(), GravekeeperHeraldEntity.setAttributes());
        event.put(ModEntities.GRAVEKEEPER_ACOLYTE.get(), GravekeeperAcolyteEntity.setAttributes());
        event.put(GRAVEKEEPER_SCHISM.get(), GravekeeperSchismEntity.setAttributes());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(GRAVEKEEPER_SCHISM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GravekeeperSchismEntity::checkSchismSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(GRAVEKEEPER_ACOLYTE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GravekeeperAcolyteEntity::checkAcolyteSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }
}
