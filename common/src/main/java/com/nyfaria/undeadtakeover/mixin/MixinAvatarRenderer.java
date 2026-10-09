package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.client.MothShoulderRenderData;
import com.nyfaria.undeadtakeover.client.SoulStolenRenderData;
import com.nyfaria.undeadtakeover.client.renderer.layers.MothShoulderLayer;
import com.nyfaria.undeadtakeover.client.renderer.layers.SkeletonHeadLayer;
import com.nyfaria.undeadtakeover.entity.data.MothAttachState;
import com.nyfaria.undeadtakeover.entity.data.SoulStolenState;
import com.nyfaria.undeadtakeover.platform.Services;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class MixinAvatarRenderer {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void undeadtakeover$addMothShoulderLayer(EntityRendererProvider.Context context, boolean slimSteve, CallbackInfo ci) {
        AvatarRenderer<?> self = (AvatarRenderer<?>) (Object) this;
        @SuppressWarnings("unchecked")
        LivingEntityRendererAccessor<AvatarRenderState, PlayerModel> accessor = (LivingEntityRendererAccessor<AvatarRenderState, PlayerModel>) (Object) this;
        accessor.undeadtakeover$addLayer(new MothShoulderLayer(self, context));
        accessor.undeadtakeover$addLayer(new SkeletonHeadLayer(self, context));
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void undeadtakeover$extractMothShoulder(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if (entity instanceof Player player) {
            MothAttachState moth = Services.MOTH_SHOULDER.get(player);
            MothShoulderRenderData mothData = (MothShoulderRenderData) state;

            mothData.undeadtakeover$setMothOnShoulder(true, moth.left());
            mothData.undeadtakeover$setMothOnShoulder(false, moth.right());

            SoulStolenState soulState = Services.SOUL_STOLEN.get(player);
            SoulStolenRenderData soulData = (SoulStolenRenderData) state;

            soulData.undeadtakeover$setFaceless(soulState.faceless());
        }
    }
}
