package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.RenderPassInfo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public abstract class ProceduralGeoRenderer<T extends LivingEntity & GeoAnimatable, R extends LivingEntityRenderState> extends GeoEntityRenderer<T, R> {

    protected ProceduralGeoRenderer(EntityRendererProvider.Context context, GeoModel<T> model) {
        super(context, model);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void adjustModelBonesForRender(RenderPassInfo renderPassInfo, BoneSnapshots snapshots) {
        this.animateBones((R) renderPassInfo.renderState(), snapshots);
    }

    protected abstract void animateBones(R renderState, BoneSnapshots snapshots);

    protected void rotateHead(BoneSnapshots snapshots, R renderState, String... bones) {
        for (String bone : bones) {
            snapshots.ifPresent(bone, snapshot -> snapshot
                    .setRotX(-renderState.xRot * Mth.DEG_TO_RAD)
                    .setRotY(-renderState.yRot * Mth.DEG_TO_RAD));
        }
    }

    protected void swingLegs(BoneSnapshots snapshots, R renderState, float phase, String... bones) {
        float angle = Mth.cos(renderState.walkAnimationPos * 0.6662F + phase) * 1.4F * renderState.walkAnimationSpeed;

        for (String bone : bones) {
            snapshots.ifPresent(bone, snapshot -> snapshot.setRotX(angle));
        }
    }

    protected void wagTail(BoneSnapshots snapshots, R renderState, String... bones) {
        float angle = Mth.cos(renderState.walkAnimationPos * 0.6662F) * 0.7F * renderState.walkAnimationSpeed;

        for (String bone : bones) {
            snapshots.ifPresent(bone, snapshot -> snapshot.setRotY(angle));
        }
    }
}
