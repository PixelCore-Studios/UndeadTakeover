package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class BiomeInit {
    public static final ResourceKey<Biome> ROTTING_PLAINS = register("rotting_plains");
    public static final ResourceKey<Biome> BLOOD_BIRCH_FOREST = register("blood_birch_forest");
    public static final ResourceKey<Biome> BLOOD_VALLEY = register("blood_valley");

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME, Constants.modLoc(name));
    }

    public static void loadClass() {
    }
}
