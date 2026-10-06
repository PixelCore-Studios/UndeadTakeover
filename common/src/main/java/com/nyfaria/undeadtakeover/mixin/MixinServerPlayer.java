package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.entity.data.MothAttachRemovalGate;
import com.nyfaria.undeadtakeover.entity.data.MothAttachState;
import com.nyfaria.undeadtakeover.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer implements MothAttachRemovalGate {
    @Unique
    private boolean undeadtakeover$mothRemovalAuthorized = false;

    @Override
    public void undeadtakeover$authorizeMothRemoval() {
        this.undeadtakeover$mothRemovalAuthorized = true;
    }

    @Inject(method = "removeEntitiesOnShoulder", at = @At("HEAD"), cancellable = true)
    private void undeadtakeover$guardMothRemoval(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        MothAttachState state = Services.MOTH_SHOULDER.get(player);

        if (!state.isEmpty() && !this.undeadtakeover$mothRemovalAuthorized) {
            ci.cancel();
        }
    }

    @Inject(method = "removeEntitiesOnShoulder", at = @At("TAIL"))
    private void undeadtakeover$clearMothShoulderFlags(CallbackInfo ci) {
        this.undeadtakeover$mothRemovalAuthorized = false;

        ServerPlayer player = (ServerPlayer) (Object) this;
        MothAttachState state = Services.MOTH_SHOULDER.get(player);

        if (state.left() && player.getShoulderEntityLeft().isEmpty()) {
            state = state.withLeft(false);
        }

        if (state.right() && player.getShoulderEntityRight().isEmpty()) {
            state = state.withRight(false);
        }

        Services.MOTH_SHOULDER.set(player, state);
    }
}
