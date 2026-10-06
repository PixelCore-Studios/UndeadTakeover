package com.nyfaria.undeadtakeover.platform.services;

import com.nyfaria.undeadtakeover.entity.data.MothAttachState;
import net.minecraft.world.entity.player.Player;

public interface IMothAttachHelper {
    MothAttachState get(Player player);

    void set(Player player, MothAttachState state);
}
