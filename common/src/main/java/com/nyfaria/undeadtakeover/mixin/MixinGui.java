package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.init.BlockInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Gui.class)
public class MixinGui {
    @Unique
    private boolean undeadtakeover$inRevenantPortal;

    @ModifyArg(method = "extractPortalOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockStateModelSet;getParticleMaterial(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/sprite/Material$Baked;"))
    private BlockState undeadtakeover$revenantPortalOverlay(BlockState state) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player != null && player.portalProcess != null) {
            this.undeadtakeover$inRevenantPortal = player.portalProcess.isSamePortal(BlockInit.REVENANT_PORTAL.get());
        }

        return this.undeadtakeover$inRevenantPortal ? BlockInit.REVENANT_PORTAL.get().defaultBlockState() : state;
    }
}
