package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.DecayingBody;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class DecayingBodyModel extends DefaultedEntityGeoModel<DecayingBody> {
    public DecayingBodyModel() {
        super(EntityInit.DECAYING_BODY.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
