package com.nyfaria.undeadtakeover;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MODID)
public class UndeadTakeover {

    public UndeadTakeover(IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();
    }
}