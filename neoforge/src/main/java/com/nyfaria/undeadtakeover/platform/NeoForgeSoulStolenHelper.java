package com.nyfaria.undeadtakeover.platform;

import com.nyfaria.undeadtakeover.entity.data.SoulStolenState;
import com.nyfaria.undeadtakeover.init.DataAttachmentsIni;
import com.nyfaria.undeadtakeover.platform.services.ISoulStolenHelper;
import net.minecraft.world.entity.player.Player;

public class NeoForgeSoulStolenHelper implements ISoulStolenHelper {

    @Override
    public SoulStolenState get(Player player) {
        return player.getData(DataAttachmentsIni.SOUL_STOLEN);
    }

    @Override
    public void set(Player player, SoulStolenState state) {
        player.setData(DataAttachmentsIni.SOUL_STOLEN, state);
    }
}
