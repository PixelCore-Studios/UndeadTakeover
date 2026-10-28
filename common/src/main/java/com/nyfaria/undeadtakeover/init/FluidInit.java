package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.platform.Services;
import com.nyfaria.undeadtakeover.registration.RegistrationProvider;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public class FluidInit {
    public static final RegistrationProvider<Fluid> FLUIDS = RegistrationProvider.get(Registries.FLUID, Constants.MODID);

    public static final RegistryObject<Fluid, FlowingFluid> BLOOD = FLUIDS.register("blood", Services.FLUIDS::createBloodSource);
    public static final RegistryObject<Fluid, FlowingFluid> FLOWING_BLOOD = FLUIDS.register("flowing_blood", Services.FLUIDS::createBloodFlowing);

    public static void loadClass() {
    }
}
