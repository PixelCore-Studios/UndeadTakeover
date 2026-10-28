package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.Wiggler;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class WigglerModel extends DefaultedEntityGeoModel<Wiggler> {
    public WigglerModel() {
        super(EntityInit.WIGGLER.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
