package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.UndeadSheep;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class UndeadSheepModel extends DefaultedEntityGeoModel<UndeadSheep> {
    public UndeadSheepModel() {
        super(EntityInit.UNDEAD_SHEEP.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
