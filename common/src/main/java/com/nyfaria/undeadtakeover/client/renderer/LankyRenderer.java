package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.nyfaria.undeadtakeover.client.model.LankyModel;
import com.nyfaria.undeadtakeover.entity.Lanky;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class LankyRenderer extends GeoEntityRenderer<Lanky, LivingEntityRenderState> {
    public LankyRenderer(EntityRendererProvider.Context context) {
        super(context, new LankyModel());
    }
}
