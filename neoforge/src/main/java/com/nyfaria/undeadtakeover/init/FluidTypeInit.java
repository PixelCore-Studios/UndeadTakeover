package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FluidTypeInit {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Constants.MODID);

    public static final DeferredHolder<FluidType, FluidType> BLOOD = FLUID_TYPES.register("blood", () -> new FluidType(FluidType.Properties.create()
            .descriptionId("block." + Constants.MODID + ".blood")
            .canSwim(true)
            .canDrown(true)
            .canExtinguish(true)
            .canConvertToSource(true)
            .supportsBoating(true)
            .canHydrate(true)
            .isWaterLike(true)
            .motionScale(0.007)
            .fallDistanceModifier(0.0F)
            .density(1060)
            .viscosity(4000)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)));

    public static void register(IEventBus modEventBus) {
        FLUID_TYPES.register(modEventBus);
    }
}
