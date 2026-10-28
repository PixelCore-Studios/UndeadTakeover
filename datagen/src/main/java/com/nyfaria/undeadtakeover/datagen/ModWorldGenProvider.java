package com.nyfaria.undeadtakeover.datagen;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.init.BiomeInit;
import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.DimensionInit;
import com.nyfaria.undeadtakeover.init.EntityInit;
import com.nyfaria.undeadtakeover.init.FeatureInit;
import com.nyfaria.undeadtakeover.init.FluidInit;
import com.nyfaria.undeadtakeover.init.StructureInit;
import com.nyfaria.undeadtakeover.init.TagInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TimelineTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
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
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLogsDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.timeline.Timeline;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {

    private static final ResourceKey<PlacedFeature> BLOOD_BIRCH_FOREST_TREES_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Constants.modLoc("blood_birch_forest_trees"));
    private static final ResourceKey<PlacedFeature> BLOOD_VALLEY_TREES_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Constants.modLoc("blood_valley_trees"));
    private static final ResourceKey<PlacedFeature> ROTTING_PLAINS_TREES_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Constants.modLoc("rotting_plains_trees"));
    private static final ResourceKey<PlacedFeature> BLOOD_SPRING_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Constants.modLoc("blood_spring"));
    private static final ResourceKey<PlacedFeature> BLOOD_LAKE_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Constants.modLoc("blood_lake"));
    private static final ResourceKey<PlacedFeature> BLOOD_LAKE_VALLEY_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Constants.modLoc("blood_lake_valley"));
    private static final ResourceKey<PlacedFeature> BLOOD_DISK_SAND_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Constants.modLoc("blood_disk_sand"));
    private static final ResourceKey<PlacedFeature> BLOOD_DISK_CLAY_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Constants.modLoc("blood_disk_clay"));

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, ModWorldGenProvider::addDimensionTypes)
            .add(Registries.NOISE_SETTINGS, ModWorldGenProvider::addNoiseSettings)
            .add(Registries.CONFIGURED_FEATURE, ModWorldGenProvider::addConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, ModWorldGenProvider::addPlacedFeatures)
            .add(Registries.BIOME, ModWorldGenProvider::addBiomes)
            .add(Registries.LEVEL_STEM, ModWorldGenProvider::addDimensions)
            .add(Registries.TEMPLATE_POOL, ModWorldGenProvider::addTemplatePools)
            .add(Registries.STRUCTURE, ModWorldGenProvider::addStructures)
            .add(Registries.STRUCTURE_SET, ModWorldGenProvider::addStructureSets);

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

    public static void addNoiseSettings(BootstrapContext<NoiseGeneratorSettings> context) {
        context.register(DimensionInit.THE_REVENANT_NOISE, new NoiseGeneratorSettings(
                NoiseSettings.create(-64, 384, 1, 2),
                Blocks.STONE.defaultBlockState(),
                BlockInit.BLOOD.get().defaultBlockState(),
                NoiseRouterData.overworld(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE), false, false),
                SurfaceRuleData.overworld(),
                new OverworldBiomeBuilder().spawnTarget(),
                63,
                false,
                true,
                true,
                false
        ));
    }

    public static void addConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(FeatureInit.BLOOD_BIRCH_TREE, new ConfiguredFeature<>(Feature.TREE, bloodBirchTree(0).build()));
        context.register(FeatureInit.TALL_BLOOD_BIRCH_TREE, new ConfiguredFeature<>(Feature.TREE, bloodBirchTree(6).build()));
        context.register(FeatureInit.FALLEN_BLOOD_BIRCH_TREE, new ConfiguredFeature<>(Feature.FALLEN_TREE,
                new FallenTreeConfiguration.FallenTreeConfigurationBuilder(BlockStateProvider.simple(BlockInit.BLOOD_BIRCH_LOG.get()), UniformInt.of(5, 8))
                        .logDecorators(List.of(new AttachedToLogsDecorator(
                                0.1F,
                                new WeightedStateProvider(WeightedList.<BlockState>builder()
                                        .add(Blocks.RED_MUSHROOM.defaultBlockState(), 2)
                                        .add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1)),
                                List.of(Direction.UP))))
                        .build()));

        Holder<PlacedFeature> bloodBirch = treeOnSapling(configuredFeatures.getOrThrow(FeatureInit.BLOOD_BIRCH_TREE));
        Holder<PlacedFeature> tallBloodBirch = treeOnSapling(configuredFeatures.getOrThrow(FeatureInit.TALL_BLOOD_BIRCH_TREE));
        Holder<PlacedFeature> fallenBloodBirch = treeOnSapling(configuredFeatures.getOrThrow(FeatureInit.FALLEN_BLOOD_BIRCH_TREE));

        context.register(FeatureInit.BLOOD_BIRCH_FOREST_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(
                new WeightedPlacedFeature(fallenBloodBirch, 0.02F),
                new WeightedPlacedFeature(tallBloodBirch, 0.35F)
        ), bloodBirch)));
        context.register(FeatureInit.SPARSE_BLOOD_BIRCH_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(
                new WeightedPlacedFeature(fallenBloodBirch, 0.25F)
        ), bloodBirch)));

        context.register(FeatureInit.BLOOD_SPRING, new ConfiguredFeature<>(Feature.SPRING, new SpringConfiguration(
                FluidInit.BLOOD.get().defaultFluidState(),
                true,
                4,
                1,
                HolderSet.direct(Block::builtInRegistryHolder, Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DEEPSLATE, Blocks.TUFF, Blocks.CALCITE, Blocks.DIRT)
        )));
        context.register(FeatureInit.BLOOD_LAKE, new ConfiguredFeature<>(Feature.LAKE, new LakeFeature.Configuration(
                BlockStateProvider.simple(BlockInit.BLOOD.get()),
                BlockStateProvider.simple(Blocks.STONE)
        )));
        context.register(FeatureInit.BLOOD_DISK_SAND, new ConfiguredFeature<>(Feature.DISK, new DiskConfiguration(
                BlockStateProvider.simple(Blocks.SAND),
                BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK)),
                UniformInt.of(2, 6),
                2
        )));
        context.register(FeatureInit.BLOOD_DISK_CLAY, new ConfiguredFeature<>(Feature.DISK, new DiskConfiguration(
                BlockStateProvider.simple(Blocks.CLAY),
                BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.CLAY)),
                UniformInt.of(2, 3),
                1
        )));
    }

    public static void addPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> forestTrees = configuredFeatures.getOrThrow(FeatureInit.BLOOD_BIRCH_FOREST_TREES);
        Holder<ConfiguredFeature<?, ?>> sparseTrees = configuredFeatures.getOrThrow(FeatureInit.SPARSE_BLOOD_BIRCH_TREES);

        context.register(BLOOD_BIRCH_FOREST_TREES_PLACED, new PlacedFeature(forestTrees, treePlacement(PlacementUtils.countExtra(10, 0.1F, 1))));
        context.register(BLOOD_VALLEY_TREES_PLACED, new PlacedFeature(sparseTrees, treePlacement(PlacementUtils.countExtra(2, 0.1F, 1))));
        context.register(ROTTING_PLAINS_TREES_PLACED, new PlacedFeature(sparseTrees, treePlacement(PlacementUtils.countExtra(0, 0.05F, 1))));

        context.register(BLOOD_SPRING_PLACED, new PlacedFeature(configuredFeatures.getOrThrow(FeatureInit.BLOOD_SPRING), List.of(
                CountPlacement.of(25),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(192)),
                BiomeFilter.biome()
        )));

        Holder<ConfiguredFeature<?, ?>> bloodLake = configuredFeatures.getOrThrow(FeatureInit.BLOOD_LAKE);
        context.register(BLOOD_LAKE_PLACED, new PlacedFeature(bloodLake, List.of(
                RarityFilter.onAverageOnceEvery(40),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
        )));
        context.register(BLOOD_LAKE_VALLEY_PLACED, new PlacedFeature(bloodLake, List.of(
                RarityFilter.onAverageOnceEvery(8),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
        )));

        context.register(BLOOD_DISK_SAND_PLACED, new PlacedFeature(configuredFeatures.getOrThrow(FeatureInit.BLOOD_DISK_SAND), List.of(
                CountPlacement.of(3),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(FluidInit.BLOOD.get())),
                BiomeFilter.biome()
        )));
        context.register(BLOOD_DISK_CLAY_PLACED, new PlacedFeature(configuredFeatures.getOrThrow(FeatureInit.BLOOD_DISK_CLAY), List.of(
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(FluidInit.BLOOD.get())),
                BiomeFilter.biome()
        )));
    }

    private static TreeConfiguration.TreeConfigurationBuilder bloodBirchTree(int extraHeight) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BlockInit.BLOOD_BIRCH_LOG.get()),
                new StraightTrunkPlacer(5, 2, extraHeight),
                BlockStateProvider.simple(BlockInit.BLOOD_BIRCH_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines();
    }

    private static Holder<PlacedFeature> treeOnSapling(Holder<ConfiguredFeature<?, ?>> tree) {
        return PlacementUtils.inlinePlaced(tree, PlacementUtils.filteredByBlockSurvival(BlockInit.BLOOD_BIRCH_SAPLING.get()));
    }

    private static List<PlacementModifier> treePlacement(PlacementModifier count) {
        return List.of(
                count,
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome(),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(BlockInit.BLOOD_BIRCH_SAPLING.get().defaultBlockState(), BlockPos.ZERO))
        );
    }

    public static void addBiomes(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(BiomeInit.ROTTING_PLAINS, rottingPlains(placedFeatures, carvers));
        context.register(BiomeInit.BLOOD_BIRCH_FOREST, bloodBirchForest(placedFeatures, carvers));
        context.register(BiomeInit.BLOOD_VALLEY, bloodValley(placedFeatures, carvers));
    }

    public static void addDimensions(BootstrapContext<LevelStem> context) {
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);

        MultiNoiseBiomeSource biomeSource = MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(List.of(
                Pair.of(Climate.parameters(0.3F, -0.4F, 0.1F, 0.2F, 0.0F, 0.0F, 0.0F), biomes.getOrThrow(BiomeInit.ROTTING_PLAINS)),
                Pair.of(Climate.parameters(-0.2F, 0.4F, 0.2F, 0.0F, 0.0F, 0.0F, 0.0F), biomes.getOrThrow(BiomeInit.BLOOD_BIRCH_FOREST)),
                Pair.of(Climate.parameters(0.4F, 0.4F, -0.3F, -0.3F, 0.0F, 0.0F, 0.0F), biomes.getOrThrow(BiomeInit.BLOOD_VALLEY))
        )));

        context.register(DimensionInit.THE_REVENANT_STEM, new LevelStem(
                dimensionTypes.getOrThrow(DimensionInit.THE_REVENANT_TYPE),
                new NoiseBasedChunkGenerator(biomeSource, noiseSettings.getOrThrow(DimensionInit.THE_REVENANT_NOISE))
        ));
    }

    public static void addTemplatePools(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);

        context.register(StructureInit.GREAT_MAUSOLEUM_POOL, new StructureTemplatePool(
                empty,
                List.of(Pair.of(
                        StructurePoolElement.single(Constants.modLoc("great_mausoleum/great_mausoleum").toString()), 1
                )),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(StructureInit.REVENANT_PORTAL_POOL, new StructureTemplatePool(
                empty,
                List.of(Pair.of(
                        StructurePoolElement.single(Constants.modLoc("graveyard/portal/revanent_portal").toString()), 1
                )),
                StructureTemplatePool.Projection.RIGID
        ));
    }

    public static void addStructures(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);

        context.register(StructureInit.GREAT_MAUSOLEUM, new JigsawStructure(
                new Structure.StructureSettings.Builder(HolderSet.direct(biomes.getOrThrow(BiomeInit.ROTTING_PLAINS)))
                        .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                        .build(),
                templates.getOrThrow(StructureInit.GREAT_MAUSOLEUM_POOL),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                false,
                Heightmap.Types.WORLD_SURFACE_WG
        ));

        context.register(StructureInit.REVENANT_PORTAL, new JigsawStructure(
                new Structure.StructureSettings.Builder(biomes.getOrThrow(TagInit.HAS_REVENANT_PORTAL))
                        .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                        .build(),
                templates.getOrThrow(StructureInit.REVENANT_PORTAL_POOL),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(1)),
                false,
                Heightmap.Types.WORLD_SURFACE_WG
        ));
    }

    public static void addStructureSets(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(StructureInit.GREAT_MAUSOLEUM_SET, new StructureSet(
                structures.getOrThrow(StructureInit.GREAT_MAUSOLEUM),
                new RandomSpreadStructurePlacement(80, 20, RandomSpreadType.TRIANGULAR, 701337)
        ));

        context.register(StructureInit.REVENANT_PORTAL_SET, new StructureSet(
                structures.getOrThrow(StructureInit.REVENANT_PORTAL),
                new RandomSpreadStructurePlacement(48, 16, RandomSpreadType.LINEAR, 482917)
        ));
    }

    private static Biome rottingPlains(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = baseGeneration(placedFeatures, carvers);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ROTTING_PLAINS_TREES_PLACED);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
        BiomeDefaultFeatures.addPlainGrass(generation);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);

        return baseBiome(0.8F, 0.4F)
                .mobSpawnSettings(revenantSpawns(30, 20, 20).build())
                .generationSettings(generation.build())
                .build();
    }

    private static Biome bloodBirchForest(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = baseGeneration(placedFeatures, carvers);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, BLOOD_BIRCH_FOREST_TREES_PLACED);
        BiomeDefaultFeatures.addForestGrass(generation);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);

        return baseBiome(0.7F, 0.6F)
                .mobSpawnSettings(revenantSpawns(70, 25, 5).build())
                .generationSettings(generation.build())
                .build();
    }

    private static Biome bloodValley(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = baseGeneration(placedFeatures, carvers);
        generation.addFeature(GenerationStep.Decoration.LAKES, BLOOD_LAKE_VALLEY_PLACED);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, BLOOD_VALLEY_TREES_PLACED);
        BiomeDefaultFeatures.addForestGrass(generation);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);

        return baseBiome(0.9F, 0.7F)
                .mobSpawnSettings(revenantSpawns(35, 60, 10).build())
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
                .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0x4A0508)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x8A0F12)
                        .grassColorOverride(0x6B7A45)
                        .foliageColorOverride(0x5A6838)
                        .build());
    }

    private static BiomeGenerationSettings.Builder baseGeneration(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        generation.addFeature(GenerationStep.Decoration.LAKES, BLOOD_LAKE_PLACED);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, BLOOD_SPRING_PLACED);
        generation.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_LAVA);
        BiomeDefaultFeatures.addSurfaceFreezing(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, BLOOD_DISK_SAND_PLACED);
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, BLOOD_DISK_CLAY_PLACED);
        return generation;
    }

    private static MobSpawnSettings.Builder revenantSpawns(int wigglerWeight, int bloodSipperWeight, int gravebeakWeight) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.caveSpawns(spawns);
        spawns.addSpawn(MobCategory.AMBIENT, 15, new MobSpawnSettings.SpawnerData(EntityInit.MOTH_SOUL.get(), 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityInit.DECAYING_BODY.get(), 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, 25, new MobSpawnSettings.SpawnerData(EntityInit.DICEPHALUS.get(), 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, wigglerWeight, new MobSpawnSettings.SpawnerData(EntityInit.WIGGLER.get(), 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, bloodSipperWeight, new MobSpawnSettings.SpawnerData(EntityInit.BLOOD_SIPPER.get(), 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, gravebeakWeight, new MobSpawnSettings.SpawnerData(EntityInit.GRAVEBEAK.get(), 1, 1));
        spawns.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityInit.SHADOWED.get(), 1, 1));
        spawns.addSpawn(MobCategory.MONSTER, 15, new MobSpawnSettings.SpawnerData(EntityInit.LANKY.get(), 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, 20, new MobSpawnSettings.SpawnerData(EntityInit.SHINGLED.get(), 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, 15, new MobSpawnSettings.SpawnerData(EntityInit.SKULL_SPIRIT.get(), 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, 25, new MobSpawnSettings.SpawnerData(EntityInit.UNDEAD_SHEEP.get(), 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, 15, new MobSpawnSettings.SpawnerData(EntityInit.UNDEAD_CAT.get(), 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, 20, new MobSpawnSettings.SpawnerData(EntityInit.UNDEAD_WOLF.get(), 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, 15, new MobSpawnSettings.SpawnerData(EntityInit.UNDEAD_AXOLOTL.get(), 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, 40, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 1, 1));
        spawns.addSpawn(MobCategory.MONSTER, 40, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 1, 3));
        return spawns;
    }
}
