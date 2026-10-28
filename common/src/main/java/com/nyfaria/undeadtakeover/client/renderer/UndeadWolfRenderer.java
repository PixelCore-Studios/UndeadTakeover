package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.base.BoneSnapshots;
import com.nyfaria.undeadtakeover.client.model.UndeadWolfModel;
import com.nyfaria.undeadtakeover.entity.UndeadWolf;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class UndeadWolfRenderer extends ProceduralGeoRenderer<UndeadWolf, LivingEntityRenderState> {
    public UndeadWolfRenderer(EntityRendererProvider.Context context) {
        super(context, new UndeadWolfModel());
    }

    @Override
    protected void animateBones(LivingEntityRenderState renderState, BoneSnapshots snapshots) {
        this.rotateHead(snapshots, renderState, "head");
        this.swingLegs(snapshots, renderState, 0.0F, "leg1", "leg4");
        this.swingLegs(snapshots, renderState, Mth.PI, "leg2", "leg3");
        this.wagTail(snapshots, renderState, "tail");
    }
}
