package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.Dicephalus;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class DicephalusModel extends DefaultedEntityGeoModel<Dicephalus> {
    public DicephalusModel() {
        super(EntityInit.DICEPHALUS.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
