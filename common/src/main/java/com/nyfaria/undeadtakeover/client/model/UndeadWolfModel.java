package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.UndeadWolf;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class UndeadWolfModel extends DefaultedEntityGeoModel<UndeadWolf> {
    public UndeadWolfModel() {
        super(EntityInit.UNDEAD_WOLF.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
