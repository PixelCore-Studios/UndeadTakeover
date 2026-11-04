package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.init.TagInit;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.levelgen.feature.FallenTreeFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({TreeFeature.class, FallenTreeFeature.class})
public class MixinTreeFeatures {

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void undeadtakeover$noTreesInStructures(FeaturePlaceContext<?> context, CallbackInfoReturnable<Boolean> cir) {
        if (context.level() instanceof WorldGenRegion region
                && region.getLevel().structureManager().forWorldGenRegion(region).getStructureWithPieceAt(context.origin(), TagInit.NO_TREES).isValid()) {
            cir.setReturnValue(false);
        }
    }
}
