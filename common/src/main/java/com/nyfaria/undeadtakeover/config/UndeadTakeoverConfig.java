package com.nyfaria.undeadtakeover.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class UndeadTakeoverConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue MOTH_SOUL_SIPHON_INTERVAL_SECONDS;
    public static final ModConfigSpec.DoubleValue MOTH_SOUL_SIPHON_AMOUNT;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("moth_soul");
        MOTH_SOUL_SIPHON_INTERVAL_SECONDS = builder.comment("Seconds a Moth Soul must sit on a player before it drains more max health")
                .defineInRange("siphon_interval_seconds", 30, 1, 3600);
        MOTH_SOUL_SIPHON_AMOUNT = builder.comment("Max health drained per interval while a Moth Soul is sitting on a player")
                .defineInRange("siphon_amount", 1.0D, 0.0D, 20.0D);
        builder.pop();

        SPEC = builder.build();
    }

    private UndeadTakeoverConfig() {
    }

    public static int siphonIntervalTicks() {
        return (SPEC.isLoaded() ? MOTH_SOUL_SIPHON_INTERVAL_SECONDS.get() : MOTH_SOUL_SIPHON_INTERVAL_SECONDS.getDefault()) * 20;
    }

    public static double siphonAmount() {
        return SPEC.isLoaded() ? MOTH_SOUL_SIPHON_AMOUNT.get() : MOTH_SOUL_SIPHON_AMOUNT.getDefault();
    }
}
