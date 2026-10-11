package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootTableInit {
    public static final ResourceKey<LootTable> GREAT_MAUSOLEUM_CHEST = ResourceKey.create(Registries.LOOT_TABLE, Constants.modLoc("chests/great_mausoleum"));

    public static void loadClass() {
    }
}
