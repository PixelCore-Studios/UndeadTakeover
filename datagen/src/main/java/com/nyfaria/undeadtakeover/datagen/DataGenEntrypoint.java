package com.nyfaria.undeadtakeover.datagen;

import com.nyfaria.undeadtakeover.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Constants.MODID)
public class DataGenEntrypoint {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ModWorldGenProvider worldGenProvider = generator.addProvider(true, new ModWorldGenProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModDamageTypeProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModRecipeProvider.Runner(packOutput, lookupProvider));
        generator.addProvider(true, new ModLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModSoundProvider(packOutput));
        generator.addProvider(true, new ModTagProvider.ModBlockTags(packOutput, lookupProvider));
        generator.addProvider(true, new ModTagProvider.ModItemTags(packOutput, lookupProvider));
        generator.addProvider(true, new ModTagProvider.ModBiomeTags(packOutput, worldGenProvider.getRegistryProvider()));
        generator.addProvider(true, new ModTagProvider.ModEntityTypeTags(packOutput, lookupProvider));
        generator.addProvider(true, new ModModelProvider(packOutput));
        generator.addProvider(true, new ModLangProvider(packOutput));
    }
}
