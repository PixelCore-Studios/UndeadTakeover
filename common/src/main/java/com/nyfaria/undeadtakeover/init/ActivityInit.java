package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.registration.RegistrationProvider;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.schedule.Activity;

public class ActivityInit {
    private static final RegistrationProvider<Activity> ACTIVITIES = RegistrationProvider.get(Registries.ACTIVITY, Constants.MODID);

    public static final RegistryObject<Activity, Activity> CARRY = registerActivity("carry");

    private static RegistryObject<Activity, Activity> registerActivity(String name) {
        return ACTIVITIES.register(name, () -> new Activity(name));
    }

    public static void loadClass() {
    }
}
