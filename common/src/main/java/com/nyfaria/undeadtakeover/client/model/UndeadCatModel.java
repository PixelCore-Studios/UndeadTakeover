package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.UndeadCat;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class UndeadCatModel extends DefaultedEntityGeoModel<UndeadCat> {
    public UndeadCatModel() {
        super(EntityInit.UNDEAD_CAT.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
