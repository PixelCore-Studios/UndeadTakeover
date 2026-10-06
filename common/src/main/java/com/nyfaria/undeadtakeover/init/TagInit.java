package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class TagInit {
    public static final TagKey<Biome> THE_REVENANT_BIOMES = TagKey.create(Registries.BIOME, Constants.modLoc("the_revenant"));
    public static final TagKey<Item> HEALTH_RESTORE_FOODS = TagKey.create(Registries.ITEM, Constants.modLoc("health_restore_foods"));

    public static void loadClass() {
    }
}
