package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.nyfaria.undeadtakeover.client.model.SkullSpiritModel;
import com.nyfaria.undeadtakeover.entity.SkullSpirit;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class SkullSpiritRenderer extends GeoEntityRenderer<SkullSpirit, LivingEntityRenderState> {
    public SkullSpiritRenderer(EntityRendererProvider.Context context) {
        super(context, new SkullSpiritModel());
    }
}
