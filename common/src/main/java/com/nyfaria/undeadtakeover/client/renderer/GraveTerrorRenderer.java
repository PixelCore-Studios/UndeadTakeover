package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.nyfaria.undeadtakeover.client.model.GraveTerrorModel;
import com.nyfaria.undeadtakeover.entity.GraveTerror;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class GraveTerrorRenderer extends GeoEntityRenderer<GraveTerror, LivingEntityRenderState> {
    public GraveTerrorRenderer(EntityRendererProvider.Context context) {
        super(context, new GraveTerrorModel());
    }
}
