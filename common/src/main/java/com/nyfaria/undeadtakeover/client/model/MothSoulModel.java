package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.MothSoul;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class MothSoulModel extends DefaultedEntityGeoModel<MothSoul> {
    public MothSoulModel() {
        super(EntityInit.MOTH_SOUL.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
