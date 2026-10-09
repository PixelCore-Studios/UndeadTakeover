package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.Shadowed;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class ShadowedModel extends DefaultedEntityGeoModel<Shadowed> {
    public ShadowedModel() {
        super(EntityInit.SHADOWED.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
