package com.nyfaria.undeadtakeover;

import com.nyfaria.undeadtakeover.client.renderer.BloodSipperRenderer;
import com.nyfaria.undeadtakeover.init.EntityInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class UndeadTakeoverClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityInit.BLOOD_SIPPER.get(), BloodSipperRenderer::new);
    }
}
