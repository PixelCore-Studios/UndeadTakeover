package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class StructureInit {
    public static final ResourceKey<StructureTemplatePool> GREAT_MAUSOLEUM_POOL = ResourceKey.create(Registries.TEMPLATE_POOL, Constants.modLoc("great_mausoleum"));
    public static final ResourceKey<Structure> GREAT_MAUSOLEUM = ResourceKey.create(Registries.STRUCTURE, Constants.modLoc("great_mausoleum"));
    public static final ResourceKey<StructureSet> GREAT_MAUSOLEUM_SET = ResourceKey.create(Registries.STRUCTURE_SET, Constants.modLoc("great_mausoleum"));

    public static void loadClass() {
    }
}
