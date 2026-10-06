package com.nyfaria.undeadtakeover.platform;

import com.nyfaria.undeadtakeover.init.DataAttachmentsIni;
import com.nyfaria.undeadtakeover.entity.data.MothAttachState;
import com.nyfaria.undeadtakeover.platform.services.IMothAttachHelper;
import net.minecraft.world.entity.player.Player;

public class NeoForgeMothAttachHelper implements IMothAttachHelper {

    @Override
    public MothAttachState get(Player player) {
        return player.getData(DataAttachmentsIni.MOTH_SHOULDER);
    }

    @Override
    public void set(Player player, MothAttachState state) {
        player.setData(DataAttachmentsIni.MOTH_SHOULDER, state);
    }
}
