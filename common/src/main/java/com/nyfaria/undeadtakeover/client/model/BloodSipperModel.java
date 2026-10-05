package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.BloodSipper;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class BloodSipperModel extends DefaultedEntityGeoModel<BloodSipper> {
    public BloodSipperModel() {
        super(EntityInit.BLOOD_SIPPER.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
