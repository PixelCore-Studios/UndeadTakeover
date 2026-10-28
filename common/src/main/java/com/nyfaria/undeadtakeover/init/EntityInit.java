package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.entity.BloodSipper;
import com.nyfaria.undeadtakeover.entity.DecayingBody;
import com.nyfaria.undeadtakeover.entity.Dicephalus;
import com.nyfaria.undeadtakeover.entity.GraveTerror;
import com.nyfaria.undeadtakeover.entity.Gravebeak;
import com.nyfaria.undeadtakeover.entity.Lanky;
import com.nyfaria.undeadtakeover.entity.MothSoul;
import com.nyfaria.undeadtakeover.entity.Shadowed;
import com.nyfaria.undeadtakeover.entity.Shingled;
import com.nyfaria.undeadtakeover.entity.SkullSpirit;
import com.nyfaria.undeadtakeover.entity.UndeadAxolotl;
import com.nyfaria.undeadtakeover.entity.UndeadCat;
import com.nyfaria.undeadtakeover.entity.UndeadSheep;
import com.nyfaria.undeadtakeover.entity.UndeadWolf;
import com.nyfaria.undeadtakeover.entity.Wiggler;
import com.nyfaria.undeadtakeover.registration.RegistrationProvider;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EntityInit {
    public static final RegistrationProvider<EntityType<?>> ENTITIES = RegistrationProvider.get(Registries.ENTITY_TYPE, Constants.MODID);
    public static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();
    public static final List<SpawnPlacementRegister<?>> spawnPlacements = new ArrayList<>();

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

    public static final RegistryObject<EntityType<?>, EntityType<DecayingBody>> DECAYING_BODY = registerLivingEntity("decaying_body",
            () -> EntityType.Builder.of(DecayingBody::new, MobCategory.MONSTER).sized(0.6F, 1.95F),
            DecayingBody::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<Dicephalus>> DICEPHALUS = registerLivingEntity("dicephalus",
            () -> EntityType.Builder.of(Dicephalus::new, MobCategory.MONSTER).sized(0.6F, 1.95F),
            Dicephalus::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<Wiggler>> WIGGLER = registerLivingEntity("wiggler",
            () -> EntityType.Builder.of(Wiggler::new, MobCategory.MONSTER).sized(0.6F, 1.6F),
            Wiggler::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<Lanky>> LANKY = registerLivingEntity("lanky",
            () -> EntityType.Builder.of(Lanky::new, MobCategory.MONSTER).sized(0.75F, 2.9F),
            Lanky::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<Shingled>> SHINGLED = registerLivingEntity("shingled",
            () -> EntityType.Builder.of(Shingled::new, MobCategory.MONSTER).sized(0.6F, 1.7F),
            Shingled::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<SkullSpirit>> SKULL_SPIRIT = registerLivingEntity("skull_spirit",
            () -> EntityType.Builder.of(SkullSpirit::new, MobCategory.MONSTER).sized(0.5F, 1.2F),
            SkullSpirit::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<UndeadSheep>> UNDEAD_SHEEP = registerLivingEntity("undead_sheep",
            () -> EntityType.Builder.of(UndeadSheep::new, MobCategory.MONSTER).sized(0.9F, 1.3F).eyeHeight(1.235F),
            UndeadSheep::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<UndeadCat>> UNDEAD_CAT = registerLivingEntity("undead_cat",
            () -> EntityType.Builder.of(UndeadCat::new, MobCategory.MONSTER).sized(0.6F, 0.7F),
            UndeadCat::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<UndeadWolf>> UNDEAD_WOLF = registerLivingEntity("undead_wolf",
            () -> EntityType.Builder.of(UndeadWolf::new, MobCategory.MONSTER).sized(0.6F, 0.85F).eyeHeight(0.68F),
            UndeadWolf::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<UndeadAxolotl>> UNDEAD_AXOLOTL = registerLivingEntity("undead_axolotl",
            () -> EntityType.Builder.of(UndeadAxolotl::new, MobCategory.MONSTER).sized(0.75F, 0.42F).eyeHeight(0.2751F),
            UndeadAxolotl::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<GraveTerror>> GRAVE_TERROR = registerLivingEntity("grave_terror",
            () -> EntityType.Builder.of(GraveTerror::new, MobCategory.MONSTER).sized(1.6F, 2.6F).fireImmune(),
            GraveTerror::createAttributes);

    public static final RegistryObject<EntityType<?>, EntityType<Boat>> BLOOD_BIRCH_BOAT = registerEntity("blood_birch_boat",
            () -> EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, () -> BlockInit.BLOOD_BIRCH_BOAT_ITEM.get()), MobCategory.MISC)
                    .noLootTable()
                    .sized(1.375F, 0.5625F)
                    .eyeHeight(0.5625F)
                    .clientTrackingRange(10));

    public static final RegistryObject<EntityType<?>, EntityType<ChestBoat>> BLOOD_BIRCH_CHEST_BOAT = registerEntity("blood_birch_chest_boat",
            () -> EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, () -> BlockInit.BLOOD_BIRCH_CHEST_BOAT_ITEM.get()), MobCategory.MISC)
                    .noLootTable()
                    .sized(1.375F, 0.5625F)
                    .eyeHeight(0.5625F)
                    .clientTrackingRange(10));

    static {
        registerSpawnPlacement(DECAYING_BODY, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(DICEPHALUS, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(WIGGLER, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(BLOOD_SIPPER, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(GRAVEBEAK, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(SHADOWED, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(MOTH_SOUL, SpawnPlacementTypes.ON_GROUND, Mob::checkMobSpawnRules);
        registerSpawnPlacement(LANKY, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(SHINGLED, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(SKULL_SPIRIT, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(UNDEAD_SHEEP, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(UNDEAD_CAT, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(UNDEAD_WOLF, SpawnPlacementTypes.ON_GROUND, Monster::checkMonsterSpawnRules);
        registerSpawnPlacement(UNDEAD_AXOLOTL, SpawnPlacementTypes.IN_WATER, UndeadAxolotl::checkUndeadAxolotlSpawnRules);
    }

    protected static <T extends Mob> void registerSpawnPlacement(Supplier<EntityType<T>> entityTypeSupplier, SpawnPlacementType placementType, SpawnPlacements.SpawnPredicate<T> predicate) {
        spawnPlacements.add(new SpawnPlacementRegister<>(entityTypeSupplier, placementType, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate));
    }

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

    public record SpawnPlacementRegister<E extends Mob>(Supplier<EntityType<E>> entityTypeSupplier, SpawnPlacementType placementType,
                                                        Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<E> predicate) {
    }
}
