package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.registration.RegistrationProvider;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class ParticleInit {
    private static final RegistrationProvider<ParticleType<?>> PARTICLE_TYPES = RegistrationProvider.get(Registries.PARTICLE_TYPE, Constants.MODID);

    public static final RegistryObject<ParticleType<?>, SimpleParticleType> BLOOD_SMEAR = PARTICLE_TYPES.register("blood_smear", () -> new SimpleParticleType(false));

    public static void loadClass() {
    }
}
