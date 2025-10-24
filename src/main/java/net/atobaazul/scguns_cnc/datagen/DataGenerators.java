package net.atobaazul.scguns_cnc.datagen;

import net.atobaazul.scguns_cnc.datagen.providers.client.ModItemModelProvider;
import net.atobaazul.scguns_cnc.datagen.providers.client.ModLangProvider;
import net.atobaazul.scguns_cnc.datagen.providers.server.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ModBuiltInEntriesProvider datapackEntries = new ModBuiltInEntriesProvider(packOutput, lookupProvider);
        CompletableFuture<HolderLookup.Provider> provider = datapackEntries.getRegistryProvider();


        boolean server = event.includeServer();
        boolean client = event.includeClient();

        ModBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(server, new ModItemTagsProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(client, new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(client, new ModLangProvider(packOutput));
        generator.addProvider(server, new ModLootTableProvider(packOutput));
        generator.addProvider(client, new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(server, new ModEntityTagsProvider(packOutput, provider, existingFileHelper));

    }
}
