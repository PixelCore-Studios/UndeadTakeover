package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.client.MothShoulderRenderData;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class MixinAvatarRenderState implements MothShoulderRenderData {
    @Unique
    private boolean undeadtakeover$mothLeft;
    @Unique
    private boolean undeadtakeover$mothRight;

    @Override
    public boolean undeadtakeover$hasMothOnShoulder(boolean left) {
        return left ? this.undeadtakeover$mothLeft : this.undeadtakeover$mothRight;
    }

    @Override
    public void undeadtakeover$setMothOnShoulder(boolean left, boolean present) {
        if (left) {
            this.undeadtakeover$mothLeft = present;
        }
        else {
            this.undeadtakeover$mothRight = present;
        }
    }
}
