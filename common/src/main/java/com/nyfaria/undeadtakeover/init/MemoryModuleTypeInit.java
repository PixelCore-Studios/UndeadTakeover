package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.registration.RegistrationProvider;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;

public class MemoryModuleTypeInit {
    private static final RegistrationProvider<MemoryModuleType<?>> MEMORY_MODULE_TYPES = RegistrationProvider.get(Registries.MEMORY_MODULE_TYPE, Constants.MODID);

    public static final RegistryObject<MemoryModuleType<?>, MemoryModuleType<Unit>> CARRYING = registerMemoryModuleType("carrying");
    public static final RegistryObject<MemoryModuleType<?>, MemoryModuleType<BlockPos>> RAVINE_POS = registerMemoryModuleType("ravine_pos");

    private static <T> RegistryObject<MemoryModuleType<?>, MemoryModuleType<T>> registerMemoryModuleType(String path) {
        return MEMORY_MODULE_TYPES.register(path, () -> new MemoryModuleType<>(Optional.empty()));
    }

    public static void loadClass() {
    }
}
