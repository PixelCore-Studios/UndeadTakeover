package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.entity.Gravebeak;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {

    @Inject(method = "wantsToStopRiding", at = @At("HEAD"), cancellable = true)
    private void undeadtakeover$struggleAgainstGravebeak(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;

        if (player.getVehicle() instanceof Gravebeak gravebeak) {
            cir.setReturnValue(!player.level().isClientSide() && gravebeak.tryBreakFree(player.isShiftKeyDown()));
        }
    }
}
