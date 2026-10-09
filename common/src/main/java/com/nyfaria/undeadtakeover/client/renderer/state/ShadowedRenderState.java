package com.nyfaria.undeadtakeover.client.renderer.state;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

import java.util.ArrayList;
import java.util.List;

public class ShadowedRenderState extends LivingEntityRenderState {
    public final List<DrainVictim> drainVictims = new ArrayList<>();

    public record DrainVictim(AvatarRenderState avatar, float progress) {
    }
}
