package net.atobaazul.scguns_cnc.events.server.entity;

import net.atobaazul.scguns_cnc.common.entity.*;
import net.atobaazul.scguns_cnc.registries.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GRAVEKEEPER_GHOUL.get(), GravekeeperGhoulEntity.setAttributes());
        event.put(ModEntities.GRAVEKEEPER_NEOPHYTE.get(), GravekeeperNeophyteEntity.setAttributes());
        event.put(ModEntities.GRAVEKEEPER_HERALD.get(), GravekeeperHeraldEntity.setAttributes());
        event.put(ModEntities.GRAVEKEEPER_ACOLYTE.get(), GravekeeperAcolyteEntity.setAttributes());
        event.put(ModEntities.GRAVEKEEPER_SCHISM.get(), GravekeeperSchismEntity.setAttributes());

    }


}
