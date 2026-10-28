package com.nyfaria.undeadtakeover.client.model;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.nyfaria.undeadtakeover.entity.UndeadAxolotl;
import com.nyfaria.undeadtakeover.init.EntityInit;

public class UndeadAxolotlModel extends DefaultedEntityGeoModel<UndeadAxolotl> {
    public UndeadAxolotlModel() {
        super(EntityInit.UNDEAD_AXOLOTL.getId());
    }

    @Override
    protected String subtype() {
        return "entities";
    }
}
