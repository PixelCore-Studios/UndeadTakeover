package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.nyfaria.undeadtakeover.client.model.ShingledModel;
import com.nyfaria.undeadtakeover.entity.Shingled;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class ShingledRenderer extends GeoEntityRenderer<Shingled, LivingEntityRenderState> {
    public ShingledRenderer(EntityRendererProvider.Context context) {
        super(context, new ShingledModel());
    }
}
