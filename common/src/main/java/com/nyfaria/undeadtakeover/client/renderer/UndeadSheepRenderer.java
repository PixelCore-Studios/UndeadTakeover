package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.base.BoneSnapshots;
import com.nyfaria.undeadtakeover.client.model.UndeadSheepModel;
import com.nyfaria.undeadtakeover.client.renderer.state.UndeadSheepRenderState;
import com.nyfaria.undeadtakeover.entity.UndeadSheep;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

public class UndeadSheepRenderer extends ProceduralGeoRenderer<UndeadSheep, UndeadSheepRenderState> {
    private static final String[] WOOL_BONES = {"body2", "head2", "leg5", "leg6", "leg7", "leg8"};

    public UndeadSheepRenderer(EntityRendererProvider.Context context) {
        super(context, new UndeadSheepModel());
    }

    @Override
    public UndeadSheepRenderState createRenderState(UndeadSheep animatable, @Nullable Void relatedObject) {
        return new UndeadSheepRenderState();
    }

    @Override
    public void addRenderData(UndeadSheep animatable, @Nullable Void relatedObject, UndeadSheepRenderState renderState, float partialTick) {
        renderState.sheared = animatable.isSheared();
    }

    @Override
    protected void animateBones(UndeadSheepRenderState renderState, BoneSnapshots snapshots) {
        this.rotateHead(snapshots, renderState, "head", "head2");
        this.swingLegs(snapshots, renderState, 0.0F, "leg1", "leg8", "leg4", "leg5");
        this.swingLegs(snapshots, renderState, Mth.PI, "leg2", "leg7", "leg3", "leg6");

        if (renderState.sheared) {
            for (String bone : WOOL_BONES) {
                snapshots.ifPresent(bone, snapshot -> snapshot.skipRender(true).skipChildrenRender(true));
            }
        }
    }
}
