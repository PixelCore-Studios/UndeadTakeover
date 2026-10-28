package com.nyfaria.undeadtakeover.platform;

import com.nyfaria.undeadtakeover.fluid.BloodFluid;
import com.nyfaria.undeadtakeover.platform.services.IFluidHelper;
import net.minecraft.world.level.material.FlowingFluid;

public class FabricFluidHelper implements IFluidHelper {

    @Override
    public FlowingFluid createBloodSource() {
        return new BloodFluid.Source();
    }

    @Override
    public FlowingFluid createBloodFlowing() {
        return new BloodFluid.Flowing();
    }
}
