package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.client.SoulStolenRenderData;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class MixinPlayerModel {

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
    private void undeadtakeover$hideStolenFace(AvatarRenderState state, CallbackInfo ci) {
        ((PlayerModel) (Object) this).head.visible = !((SoulStolenRenderData) state).undeadtakeover$isFaceless();
    }
}
