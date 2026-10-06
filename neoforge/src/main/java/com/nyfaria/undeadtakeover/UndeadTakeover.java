package com.nyfaria.undeadtakeover;

import com.nyfaria.undeadtakeover.init.DataAttachmentsIni;
import com.nyfaria.undeadtakeover.config.UndeadTakeoverConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MODID)
public class UndeadTakeover {

    public UndeadTakeover(IEventBus eventBus, ModContainer container) {
        Constants.LOG.info("Hello NeoForge world!");
        container.registerConfig(ModConfig.Type.COMMON, UndeadTakeoverConfig.SPEC);
        DataAttachmentsIni.register(eventBus);
        CommonClass.init();
    }
}