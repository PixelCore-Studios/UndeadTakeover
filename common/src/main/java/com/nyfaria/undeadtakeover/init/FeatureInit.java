package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class FeatureInit {
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOD_BIRCH_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLoc("blood_birch_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_BLOOD_BIRCH_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLoc("tall_blood_birch_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_BLOOD_BIRCH_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLoc("fallen_blood_birch_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOD_BIRCH_FOREST_TREES = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLoc("blood_birch_forest_trees"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPARSE_BLOOD_BIRCH_TREES = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLoc("sparse_blood_birch_trees"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOD_SPRING = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLoc("blood_spring"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOD_LAKE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLoc("blood_lake"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOD_DISK_SAND = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLoc("blood_disk_sand"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOD_DISK_CLAY = ResourceKey.create(Registries.CONFIGURED_FEATURE, Constants.modLoc("blood_disk_clay"));

    public static void loadClass() {
    }
}
