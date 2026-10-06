package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.Gravebeak;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class GravebeakModel extends DefaultedEntityGeoModel<Gravebeak> {
    public GravebeakModel() {
        super(EntityInit.GRAVEBEAK.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
