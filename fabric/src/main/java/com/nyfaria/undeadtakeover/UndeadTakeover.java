package com.nyfaria.undeadtakeover;

import com.nyfaria.undeadtakeover.init.EntityInit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class UndeadTakeover implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
        EntityInit.attributeSuppliers.forEach(p -> FabricDefaultAttributeRegistry.register(p.entityTypeSupplier().get(), p.factory().get().build()));
    }
}
