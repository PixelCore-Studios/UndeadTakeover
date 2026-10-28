package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.Shingled;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class ShingledModel extends DefaultedEntityGeoModel<Shingled> {
    public ShingledModel() {
        super(EntityInit.SHINGLED.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
