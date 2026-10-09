package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.client.MothShoulderRenderData;
import com.nyfaria.undeadtakeover.client.SoulStolenRenderData;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class MixinAvatarRenderState implements MothShoulderRenderData, SoulStolenRenderData {
    @Unique
    private boolean undeadtakeover$mothLeft;
    @Unique
    private boolean undeadtakeover$mothRight;
    @Unique
    private boolean undeadtakeover$faceless;

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

    @Override
    public boolean undeadtakeover$isFaceless() {
        return this.undeadtakeover$faceless;
    }

    @Override
    public void undeadtakeover$setFaceless(boolean faceless) {
        this.undeadtakeover$faceless = faceless;
    }
}
