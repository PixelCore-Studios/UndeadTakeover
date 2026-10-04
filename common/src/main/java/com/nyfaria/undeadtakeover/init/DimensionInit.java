package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class DimensionInit {
    public static final ResourceKey<LevelStem> THE_REVENANT_STEM = ResourceKey.create(Registries.LEVEL_STEM, Constants.modLoc("the_revenant"));
    public static final ResourceKey<Level> THE_REVENANT = ResourceKey.create(Registries.DIMENSION, Constants.modLoc("the_revenant"));
    public static final ResourceKey<DimensionType> THE_REVENANT_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, Constants.modLoc("the_revenant"));

    public static void loadClass() {
    }
}
