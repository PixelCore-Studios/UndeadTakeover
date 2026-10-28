package com.nyfaria.undeadtakeover.datagen;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.init.BiomeInit;
import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.EntityInit;
import com.nyfaria.undeadtakeover.init.FluidInit;
import com.nyfaria.undeadtakeover.init.TagInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

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

            populateBlockItemTag(TagInit.BLOOD_BIRCH_LOGS_ITEM, BlockInit.BLOOD_BIRCH_LOG, BlockInit.BLOOD_BIRCH_STRIPPED_LOG, BlockInit.BLOOD_BIRCH_WOOD, BlockInit.BLOOD_BIRCH_STRIPPED_WOOD);
            tag(ItemTags.LOGS).addTag(TagInit.BLOOD_BIRCH_LOGS_ITEM);
            tag(ItemTags.LOGS_THAT_BURN).addTag(TagInit.BLOOD_BIRCH_LOGS_ITEM);
            populateBlockItemTag(ItemTags.LEAVES, BlockInit.BLOOD_BIRCH_LEAVES);
            populateBlockItemTag(ItemTags.SAPLINGS, BlockInit.BLOOD_BIRCH_SAPLING);
            populateBlockItemTag(ItemTags.PLANKS, BlockInit.BLOOD_BIRCH_PLANKS);
            populateBlockItemTag(ItemTags.WOODEN_STAIRS, BlockInit.BLOOD_BIRCH_STAIRS);
            populateBlockItemTag(ItemTags.WOODEN_SLABS, BlockInit.BLOOD_BIRCH_SLAB);
            populateBlockItemTag(ItemTags.WOODEN_FENCES, BlockInit.BLOOD_BIRCH_FENCE);
            populateBlockItemTag(ItemTags.FENCE_GATES, BlockInit.BLOOD_BIRCH_FENCE_GATE);
            populateBlockItemTag(ItemTags.WOODEN_DOORS, BlockInit.BLOOD_BIRCH_DOOR);
            populateBlockItemTag(ItemTags.WOODEN_TRAPDOORS, BlockInit.BLOOD_BIRCH_TRAPDOOR);
            populateBlockItemTag(ItemTags.WOODEN_BUTTONS, BlockInit.BLOOD_BIRCH_BUTTON);
            populateBlockItemTag(ItemTags.WOODEN_PRESSURE_PLATES, BlockInit.BLOOD_BIRCH_PRESSURE_PLATE);
            populateTag(ItemTags.SIGNS, BlockInit.BLOOD_BIRCH_SIGN_ITEM);
            populateTag(ItemTags.HANGING_SIGNS, BlockInit.BLOOD_BIRCH_HANGING_SIGN_ITEM);
            populateTag(ItemTags.BOATS, BlockInit.BLOOD_BIRCH_BOAT_ITEM);
            populateTag(ItemTags.CHEST_BOATS, BlockInit.BLOOD_BIRCH_CHEST_BOAT_ITEM);
        }

        @SafeVarargs
        public final void populateTag(TagKey<Item> tag, Supplier<? extends Item>... items) {
            for (Supplier<? extends Item> item : items) {
                tag(tag).add(BuiltInRegistries.ITEM.getResourceKey(item.get()).orElseThrow());
            }
        }

        @SafeVarargs
        public final void populateBlockItemTag(TagKey<Item> tag, Supplier<? extends Block>... blocks) {
            for (Supplier<? extends Block> block : blocks) {
                tag(tag).add(BuiltInRegistries.ITEM.getResourceKey(block.get().asItem()).orElseThrow());
            }
        }
    }

    public static class ModBlockTags extends KeyTagProvider<Block> {

        public ModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, Registries.BLOCK, lookupProvider, Constants.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            populateTag(TagInit.BLOOD_BIRCH_LOGS, BlockInit.BLOOD_BIRCH_LOG, BlockInit.BLOOD_BIRCH_STRIPPED_LOG, BlockInit.BLOOD_BIRCH_WOOD, BlockInit.BLOOD_BIRCH_STRIPPED_WOOD);
            tag(BlockTags.LOGS).addTag(TagInit.BLOOD_BIRCH_LOGS);
            tag(BlockTags.LOGS_THAT_BURN).addTag(TagInit.BLOOD_BIRCH_LOGS);
            populateTag(BlockTags.PREVENTS_NEARBY_LEAF_DECAY, BlockInit.BLOOD_BIRCH_LOG, BlockInit.BLOOD_BIRCH_STRIPPED_LOG, BlockInit.BLOOD_BIRCH_WOOD, BlockInit.BLOOD_BIRCH_STRIPPED_WOOD);
            populateTag(BlockTags.LEAVES, BlockInit.BLOOD_BIRCH_LEAVES);
            populateTag(BlockTags.SAPLINGS, BlockInit.BLOOD_BIRCH_SAPLING);
            populateTag(BlockTags.PLANKS, BlockInit.BLOOD_BIRCH_PLANKS);
            populateTag(BlockTags.WOODEN_STAIRS, BlockInit.BLOOD_BIRCH_STAIRS);
            populateTag(BlockTags.WOODEN_SLABS, BlockInit.BLOOD_BIRCH_SLAB);
            populateTag(BlockTags.WOODEN_FENCES, BlockInit.BLOOD_BIRCH_FENCE);
            populateTag(BlockTags.FENCE_GATES, BlockInit.BLOOD_BIRCH_FENCE_GATE);
            populateTag(BlockTags.WOODEN_DOORS, BlockInit.BLOOD_BIRCH_DOOR);
            populateTag(BlockTags.WOODEN_TRAPDOORS, BlockInit.BLOOD_BIRCH_TRAPDOOR);
            populateTag(BlockTags.WOODEN_BUTTONS, BlockInit.BLOOD_BIRCH_BUTTON);
            populateTag(BlockTags.WOODEN_PRESSURE_PLATES, BlockInit.BLOOD_BIRCH_PRESSURE_PLATE);
            populateTag(BlockTags.STANDING_SIGNS, BlockInit.BLOOD_BIRCH_SIGN);
            populateTag(BlockTags.WALL_SIGNS, BlockInit.BLOOD_BIRCH_WALL_SIGN);
            populateTag(BlockTags.CEILING_HANGING_SIGNS, BlockInit.BLOOD_BIRCH_HANGING_SIGN);
            populateTag(BlockTags.WALL_HANGING_SIGNS, BlockInit.BLOOD_BIRCH_WALL_HANGING_SIGN);
        }

        @SafeVarargs
        public final void populateTag(TagKey<Block> tag, Supplier<? extends Block>... blocks) {
            for (Supplier<? extends Block> block : blocks) {
                tag(tag).add(BuiltInRegistries.BLOCK.getResourceKey(block.get()).orElseThrow());
            }
        }
    }

    public static class ModFluidTags extends KeyTagProvider<Fluid> {

        public ModFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, Registries.FLUID, lookupProvider, Constants.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(TagInit.BLOOD).add(
                    BuiltInRegistries.FLUID.getResourceKey(FluidInit.BLOOD.get()).orElseThrow(),
                    BuiltInRegistries.FLUID.getResourceKey(FluidInit.FLOWING_BLOOD.get()).orElseThrow()
            );
            tag(FluidTags.WATER).addTag(TagInit.BLOOD);
        }
    }

    public static class ModEntityTypeTags extends KeyTagProvider<EntityType<?>> {

        public ModEntityTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, Registries.ENTITY_TYPE, lookupProvider, Constants.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(EntityTypeTags.BURN_IN_DAYLIGHT).add(
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.DECAYING_BODY.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.DICEPHALUS.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.WIGGLER.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.LANKY.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.SHINGLED.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.SKULL_SPIRIT.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.UNDEAD_SHEEP.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.UNDEAD_CAT.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.UNDEAD_WOLF.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.UNDEAD_AXOLOTL.get()).orElseThrow()
            );

            tag(TagInit.UNDEAD_MOBS).add(
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.ZOMBIE).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.HUSK).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.DROWNED).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.ZOMBIE_VILLAGER).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.ZOMBIFIED_PIGLIN).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.SKELETON).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.STRAY).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.WITHER_SKELETON).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.BOGGED).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.DECAYING_BODY.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.DICEPHALUS.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.WIGGLER.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.LANKY.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.SHINGLED.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.SKULL_SPIRIT.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.UNDEAD_SHEEP.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.UNDEAD_CAT.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.UNDEAD_WOLF.get()).orElseThrow(),
                    BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityInit.UNDEAD_AXOLOTL.get()).orElseThrow()
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
            tag(TagInit.HAS_REVENANT_PORTAL)
                    .addTag(BiomeTags.IS_FOREST)
                    .addTag(BiomeTags.IS_TAIGA)
                    .addTag(BiomeTags.IS_SAVANNA)
                    .addTag(BiomeTags.IS_JUNGLE)
                    .addTag(BiomeTags.IS_BADLANDS)
                    .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SNOWY_PLAINS, Biomes.MEADOW, Biomes.DESERT, Biomes.SWAMP);
        }
    }
}
