package com.nyfaria.undeadtakeover.datagen;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.init.BiomeInit;
import com.nyfaria.undeadtakeover.init.DimensionInit;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TimelineTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.timeline.Timeline;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, ModWorldGenProvider::addDimensionTypes)
            .add(Registries.BIOME, ModWorldGenProvider::addBiomes)
            .add(Registries.LEVEL_STEM, ModWorldGenProvider::addDimensions);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", Constants.MODID));
    }

    @Override
    public String getName() {
        return "World Gen";
    }

    public static void addDimensionTypes(BootstrapContext<DimensionType> context) {
        HolderGetter<Timeline> timelines = context.lookup(Registries.TIMELINE);
        HolderGetter<WorldClock> clocks = context.lookup(Registries.WORLD_CLOCK);

        context.register(DimensionInit.THE_REVENANT_TYPE, new DimensionType(
                false,
                true,
                false,
                false,
                1.0,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                0.0F,
                new DimensionType.MonsterSettings(UniformInt.of(0, 7), 0),
                DimensionType.Skybox.OVERWORLD,
                CardinalLighting.Type.DEFAULT,
                EnvironmentAttributeMap.builder()
                        .set(EnvironmentAttributes.FOG_COLOR, 0x7F8C6E)
                        .set(EnvironmentAttributes.SKY_COLOR, 0x6E7A64)
                        .set(EnvironmentAttributes.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK)
                        .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, false)
                        .set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
                        .build(),
                timelines.getOrThrow(TimelineTags.IN_OVERWORLD),
                Optional.of(clocks.getOrThrow(WorldClocks.OVERWORLD))
        ));
    }

    public static void addBiomes(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(BiomeInit.ROTTING_PLAINS, rottingPlains(placedFeatures, carvers));
        context.register(BiomeInit.BLOOD_BIRCH_FOREST, deadForest(placedFeatures, carvers));
        context.register(BiomeInit.BLOOD_VALLEY, deadForest(placedFeatures, carvers));
    }

    public static void addDimensions(BootstrapContext<LevelStem> context) {
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);

        MultiNoiseBiomeSource biomeSource = MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(List.of(
                Pair.of(Climate.parameters(0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomes.getOrThrow(BiomeInit.ROTTING_PLAINS)),
                Pair.of(Climate.parameters(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomes.getOrThrow(BiomeInit.BLOOD_BIRCH_FOREST)),
                Pair.of(Climate.parameters(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomes.getOrThrow(BiomeInit.BLOOD_VALLEY))
        )));

        context.register(DimensionInit.THE_REVENANT_STEM, new LevelStem(
                dimensionTypes.getOrThrow(DimensionInit.THE_REVENANT_TYPE),
                new NoiseBasedChunkGenerator(biomeSource, noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD))
        ));
    }

    private static Biome rottingPlains(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = baseGeneration(placedFeatures, carvers);
        BiomeDefaultFeatures.addPlainGrass(generation);

        return baseBiome(0.8F, 0.4F)
                .mobSpawnSettings(undeadSpawns().build())
                .generationSettings(generation.build())
                .build();
    }

    private static Biome deadForest(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = baseGeneration(placedFeatures, carvers);
        BiomeDefaultFeatures.addBadlandsTrees(generation);
        BiomeDefaultFeatures.addForestGrass(generation);

        return baseBiome(0.7F, 0.6F)
                .mobSpawnSettings(undeadSpawns().build())
                .generationSettings(generation.build())
                .build();
    }

    private static Biome.BiomeBuilder baseBiome(float temperature, float downfall) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(temperature)
                .downfall(downfall)
                .setAttribute(EnvironmentAttributes.SKY_COLOR, 0x6E7A64)
                .setAttribute(EnvironmentAttributes.FOG_COLOR, 0x7F8C6E)
                .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0x2B3524)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x4E5E3A)
                        .grassColorOverride(0x6B7A45)
                        .foliageColorOverride(0x5A6838)
                        .build());
    }

    private static BiomeGenerationSettings.Builder baseGeneration(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
        BiomeDefaultFeatures.addDefaultSprings(generation);
        BiomeDefaultFeatures.addSurfaceFreezing(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);
        return generation;
    }

    private static MobSpawnSettings.Builder undeadSpawns() {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.caveSpawns(spawns);
        spawns.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, 20, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 1, 1));
        spawns.addSpawn(MobCategory.MONSTER, 60, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 2, 4));
        return spawns;
    }
}
