package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.base.BoneSnapshots;
import com.nyfaria.undeadtakeover.client.model.LankyModel;
import com.nyfaria.undeadtakeover.entity.Lanky;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class LankyRenderer extends ProceduralGeoRenderer<Lanky, LivingEntityRenderState> {
    public LankyRenderer(EntityRendererProvider.Context context) {
        super(context, new LankyModel());
    }

    @Override
    protected void animateBones(LivingEntityRenderState renderState, BoneSnapshots snapshots) {
        this.swingLegs(snapshots, renderState, 0.0F, "right_leg", "left_arm");
        this.swingLegs(snapshots, renderState, Mth.PI, "left_leg", "right_arm");
    }
}
