package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.entity.BloodSipper;
import com.nyfaria.undeadtakeover.entity.Gravebeak;
import com.nyfaria.undeadtakeover.entity.MothSoul;
import com.nyfaria.undeadtakeover.entity.Shadowed;
import com.nyfaria.undeadtakeover.registration.RegistrationProvider;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EntityInit {
    public static final RegistrationProvider<EntityType<?>> ENTITIES = RegistrationProvider.get(Registries.ENTITY_TYPE, Constants.MODID);
    public static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();

    public static final RegistryObject<EntityType<?>, EntityType<BloodSipper>> BLOOD_SIPPER = registerLivingEntity("blood_sipper",
            () -> EntityType.Builder.of(BloodSipper::new, MobCategory.MONSTER).sized(0.9F, 0.9F),
            BloodSipper::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<Gravebeak>> GRAVEBEAK = registerLivingEntity("gravebeak",
            () -> EntityType.Builder.of(Gravebeak::new, MobCategory.MONSTER).sized(1.2F, 3.0F),
            Gravebeak::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<MothSoul>> MOTH_SOUL = registerLivingEntity("moth_soul",
            () -> EntityType.Builder.of(MothSoul::new, MobCategory.AMBIENT).sized(0.6F, 0.3F),
            MothSoul::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<Shadowed>> SHADOWED = registerLivingEntity("shadowed",
            () -> EntityType.Builder.of(Shadowed::new, MobCategory.MONSTER).sized(0.9F, 2.2F),
            Shadowed::createAttributes);


    protected static <T extends Entity> RegistryObject<EntityType<?>,EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(ResourceKey.create(Registries.ENTITY_TYPE, Constants.modLoc(name))));
    }

    protected static <T extends LivingEntity> RegistryObject<EntityType<?>,EntityType<T>> registerLivingEntity(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                                 Supplier<AttributeSupplier.Builder> attributeSupplier) {
        RegistryObject<EntityType<?>,EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(new AttributesRegister<>(entityTypeSupplier, attributeSupplier));
        return entityTypeSupplier;
    }

    protected static <T extends LivingEntity> RegistryObject<EntityType<?>,EntityType<T>> registerEntityWithEgg(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                                  Supplier<AttributeSupplier.Builder> attributeSupplier,
                                                                                                  int secondaryColor) {
        return registerEntityWithEgg(name, supplier, attributeSupplier, 0x392F24, secondaryColor);
    }

    protected static <T extends LivingEntity> RegistryObject<EntityType<?>,EntityType<T>> registerEntityWithEgg(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                                  Supplier<AttributeSupplier.Builder> attributeSupplier,
                                                                                                  int primaryColor, int secondaryColor) {
        RegistryObject<EntityType<?>,EntityType<T>> entityTypeSupplier = registerLivingEntity(name, supplier, attributeSupplier);
        return entityTypeSupplier;
    }

    public static void loadClass() {
    }


    public record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier,
                                                             Supplier<AttributeSupplier.Builder> factory) {
    }
}
