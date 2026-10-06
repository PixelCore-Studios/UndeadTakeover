package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.entity.ai.sensor.RavineSensor;
import com.nyfaria.undeadtakeover.registration.RegistrationProvider;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

import java.util.function.Supplier;

public class SensorTypeInit {
    private static final RegistrationProvider<SensorType<?>> SENSOR_TYPES = RegistrationProvider.get(Registries.SENSOR_TYPE, Constants.MODID);

    public static final RegistryObject<SensorType<?>, SensorType<RavineSensor>> RAVINE = registerSensorType("ravine", RavineSensor::new);

    private static <T extends Sensor<?>> RegistryObject<SensorType<?>, SensorType<T>> registerSensorType(String path, Supplier<T> factory) {
        return SENSOR_TYPES.register(path, () -> new SensorType<>(factory));
    }

    public static void loadClass() {
    }
}
