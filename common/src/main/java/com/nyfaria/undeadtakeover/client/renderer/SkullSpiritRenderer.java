package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.nyfaria.undeadtakeover.client.model.SkullSpiritModel;
import com.nyfaria.undeadtakeover.entity.SkullSpirit;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class SkullSpiritRenderer extends GeoEntityRenderer<SkullSpirit, LivingEntityRenderState> {
    public SkullSpiritRenderer(EntityRendererProvider.Context context) {
        super(context, new SkullSpiritModel());
    }

    @Override
    public @Nullable RenderType getRenderType(LivingEntityRenderState renderState, Identifier texture) {
        return RenderTypes.entityTranslucentEmissive(texture,false);
    }
}
