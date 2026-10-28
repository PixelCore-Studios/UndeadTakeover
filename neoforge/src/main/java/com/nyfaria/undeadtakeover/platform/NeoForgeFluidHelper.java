package com.nyfaria.undeadtakeover.platform;

import com.nyfaria.undeadtakeover.fluid.BloodFluid;
import com.nyfaria.undeadtakeover.init.FluidTypeInit;
import com.nyfaria.undeadtakeover.platform.services.IFluidHelper;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;

public class NeoForgeFluidHelper implements IFluidHelper {

    @Override
    public FlowingFluid createBloodSource() {
        return new BloodFluid.Source() {
            @Override
            public FluidType getFluidType() {
                return FluidTypeInit.BLOOD.get();
            }
        };
    }

    @Override
    public FlowingFluid createBloodFlowing() {
        return new BloodFluid.Flowing() {
            @Override
            public FluidType getFluidType() {
                return FluidTypeInit.BLOOD.get();
            }
        };
    }
}
