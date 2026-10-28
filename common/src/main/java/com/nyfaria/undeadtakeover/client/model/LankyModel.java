package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.Lanky;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class LankyModel extends DefaultedEntityGeoModel<Lanky> {
    public LankyModel() {
        super(EntityInit.LANKY.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
