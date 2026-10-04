package com.nyfaria.undeadtakeover;

import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.EntityInit;
import com.nyfaria.undeadtakeover.init.ItemInit;
import com.nyfaria.undeadtakeover.init.TagInit;
import com.nyfaria.undeadtakeover.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class CommonClass {

    public static void init() {
        ItemInit.loadClass();
        BlockInit.loadClass();
        EntityInit.loadClass();
        TagInit.loadClass();
    }
}