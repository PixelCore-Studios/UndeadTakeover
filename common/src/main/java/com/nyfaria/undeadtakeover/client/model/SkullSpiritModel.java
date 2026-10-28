package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.SkullSpirit;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class SkullSpiritModel extends DefaultedEntityGeoModel<SkullSpirit> {
    public SkullSpiritModel() {
        super(EntityInit.SKULL_SPIRIT.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
