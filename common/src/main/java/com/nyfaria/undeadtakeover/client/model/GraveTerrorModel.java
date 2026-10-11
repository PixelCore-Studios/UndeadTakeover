package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.GraveTerror;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class GraveTerrorModel extends DefaultedEntityGeoModel<GraveTerror> {
    public GraveTerrorModel() {
        super(EntityInit.GRAVE_TERROR.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
