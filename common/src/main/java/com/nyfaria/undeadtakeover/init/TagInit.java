package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class TagInit {
    public static final TagKey<Biome> THE_REVENANT_BIOMES = TagKey.create(Registries.BIOME, Constants.modLoc("the_revenant"));
    public static final TagKey<Biome> HAS_REVENANT_PORTAL = TagKey.create(Registries.BIOME, Constants.modLoc("has_structure/revenant_portal"));
    public static final TagKey<Item> HEALTH_RESTORE_FOODS = TagKey.create(Registries.ITEM, Constants.modLoc("health_restore_foods"));
    public static final TagKey<EntityType<?>> UNDEAD_MOBS = TagKey.create(Registries.ENTITY_TYPE, Constants.modLoc("undead_mobs"));

    public static final TagKey<Fluid> BLOOD = TagKey.create(Registries.FLUID, Constants.modLoc("blood"));

    public static final TagKey<Block> BLOOD_BIRCH_LOGS = TagKey.create(Registries.BLOCK, Constants.modLoc("blood_birch_logs"));
    public static final TagKey<Item> BLOOD_BIRCH_LOGS_ITEM = TagKey.create(Registries.ITEM, Constants.modLoc("blood_birch_logs"));

    public static void loadClass() {
    }
}
