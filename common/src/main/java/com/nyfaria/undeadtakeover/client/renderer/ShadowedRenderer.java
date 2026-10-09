package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nyfaria.undeadtakeover.client.model.ShadowedModel;
import com.nyfaria.undeadtakeover.client.renderer.state.ShadowedRenderState;
import com.nyfaria.undeadtakeover.entity.Shadowed;
import com.nyfaria.undeadtakeover.entity.data.SoulStolenState;
import com.nyfaria.undeadtakeover.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.*;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.*;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ShadowedRenderer extends GeoEntityRenderer<Shadowed, ShadowedRenderState> {
    private static final Vec3 SOUL_TARGET = new Vec3(0.0, 0.2, 0.0);

    private final PlayerModel soulModelWide;
    private final PlayerModel soulModelSlim;

    public ShadowedRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowedModel());
        this.soulModelWide = new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false);
        this.soulModelSlim = new PlayerModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
    }

    @Override
    public ShadowedRenderState createRenderState(Shadowed animatable, @Nullable Void relatedObject) {
        return new ShadowedRenderState();
    }

    @Override
    public void addRenderData(Shadowed animatable, @Nullable Void relatedObject, ShadowedRenderState renderState, float partialTick) {
        renderState.bodyRot = Mth.rotLerp(partialTick, animatable.yHeadRotO, animatable.yHeadRot);
        renderState.yRot = 0.0F;
        renderState.drainVictims.clear();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

        for (Player player : animatable.level().players()) {
            SoulStolenState state = Services.SOUL_STOLEN.get(player);

            if (!state.isDraining() || state.drainerEntityId() != animatable.getId()) {
                continue;
            }

            if (dispatcher.getRenderer(player).createRenderState(player, partialTick) instanceof AvatarRenderState avatar) {
                float progress = Mth.clamp((state.drainTicks() + partialTick) / SoulStolenState.DRAIN_DURATION_TICKS, 0.0F, 1.0F);
                renderState.drainVictims.add(new ShadowedRenderState.DrainVictim(avatar, progress));
            }
        }
    }

    @Override
    public void submit(ShadowedRenderState renderState, PoseStack poseStack, SubmitNodeCollector renderTasks, CameraRenderState cameraState) {
        super.submit(renderState, poseStack, renderTasks, cameraState);

        for (ShadowedRenderState.DrainVictim victim : renderState.drainVictims) {
            this.submitSoul(renderState, victim, poseStack, renderTasks);
        }
    }

    private void submitSoul(ShadowedRenderState renderState, ShadowedRenderState.DrainVictim victim, PoseStack poseStack, SubmitNodeCollector renderTasks) {
        AvatarRenderState avatar = victim.avatar();
        float progress = victim.progress();
        PlayerModel soulModel = avatar.skin.model() == PlayerModelType.SLIM ? this.soulModelSlim : this.soulModelWide;

        Vec3 start = new Vec3(avatar.x - renderState.x, avatar.y - renderState.y, avatar.z - renderState.z);
        Vec3 offset = start.lerp(SOUL_TARGET, progress);

        poseStack.pushPose();
        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.scale(avatar.scale, avatar.scale, avatar.scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - avatar.bodyRot));
        poseStack.scale(-0.9375F, -0.9375F, 0.9375F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        int alpha = (int) (150 * Math.min(1.0F, progress / 0.15F));
        int tint = ARGB.color(alpha, 90, 220, 190);
        renderTasks.submitModel(soulModel, avatar, poseStack, soulModel.renderType(avatar.skin.body().texturePath()), LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, tint, null, 0, null);

        poseStack.popPose();
    }

    @Override
    protected AABB getBoundingBoxForCulling(Shadowed entity) {
        AABB box = super.getBoundingBoxForCulling(entity);

        for (Player player : entity.level().players()) {
            SoulStolenState state = Services.SOUL_STOLEN.get(player);

            if (state.isDraining() && state.drainerEntityId() == entity.getId()) {
                box = box.minmax(player.getBoundingBox());
            }
        }

        return box;
    }

    @Override
    public @Nullable RenderType getRenderType(ShadowedRenderState renderState, Identifier texture) {
        return RenderTypes.entityTranslucentEmissive(texture,false);
    }
}
