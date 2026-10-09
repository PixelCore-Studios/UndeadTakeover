package com.nyfaria.undeadtakeover.datagen;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.init.BiomeInit;
import com.nyfaria.undeadtakeover.init.EntityInit;
import com.nyfaria.undeadtakeover.init.TagInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModTagProvider {

    public static class ModItemTags extends KeyTagProvider<Item> {

        public ModItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, Registries.ITEM, lookupProvider, Constants.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(TagInit.HEALTH_RESTORE_FOODS).add(
                    BuiltInRegistries.ITEM.getResourceKey(Items.GOLDEN_APPLE).orElseThrow(),
                    BuiltInRegistries.ITEM.getResourceKey(Items.ENCHANTED_GOLDEN_APPLE).orElseThrow(),
                    BuiltInRegistries.ITEM.getResourceKey(Items.GOLDEN_CARROT).orElseThrow()
            );
        }

        @SafeVarargs
        public final void populateTag(TagKey<Item> tag, Supplier<? extends Item>... items) {
            for (Supplier<? extends Item> item : items) {
                tag(tag).add(BuiltInRegistries.ITEM.getResourceKey(item.get()).orElseThrow());
            }
        }
    }

    public static class ModBlockTags extends KeyTagProvider<Block> {

        public ModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, Registries.BLOCK, lookupProvider, Constants.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {

        }

        @SafeVarargs
        public final void populateTag(TagKey<Block> tag, Supplier<? extends Block>... blocks) {
            for (Supplier<? extends Block> block : blocks) {
                tag(tag).add(BuiltInRegistries.BLOCK.getResourceKey(block.get()).orElseThrow());
            }
        }
    }

    public static class ModEntityTypeTags extends KeyTagProvider<EntityType<?>> {

        public ModEntityTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, Registries.ENTITY_TYPE, lookupProvider, Constants.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(EntityTypeTags.BURN_IN_DAYLIGHT).add(
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.DECAYING_BODY.get()).orElseThrow()
            );
        }
    }

    public static class ModBiomeTags extends KeyTagProvider<Biome> {

        public ModBiomeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, Registries.BIOME, lookupProvider, Constants.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(TagInit.THE_REVENANT_BIOMES).add(BiomeInit.ROTTING_PLAINS,BiomeInit.BLOOD_BIRCH_FOREST,BiomeInit.BLOOD_VALLEY);
        }
    }
}
