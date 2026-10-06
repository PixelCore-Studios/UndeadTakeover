package com.nyfaria.undeadtakeover;

import com.nyfaria.undeadtakeover.init.DataAttachmentsInit;
import com.nyfaria.undeadtakeover.config.UndeadTakeoverConfig;
import com.nyfaria.undeadtakeover.init.EntityInit;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.neoforged.fml.config.ModConfig;

public class UndeadTakeover implements ModInitializer {

    @Override
    public void onInitialize() {

        ConfigRegistry.INSTANCE.register(Constants.MODID, ModConfig.Type.COMMON, UndeadTakeoverConfig.SPEC);
        DataAttachmentsInit.init();
        CommonClass.init();
        EntityInit.attributeSuppliers.forEach(p -> FabricDefaultAttributeRegistry.register(p.entityTypeSupplier().get(), p.factory().get().build()));
    }
}
