package com.nyfaria.undeadtakeover.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.feline.CatSoundVariant;
import net.minecraft.world.entity.animal.feline.CatSoundVariants;
import net.minecraft.world.level.Level;

public class UndeadCat extends UndeadAnimal {
    private static final CatSoundVariant.CatSoundSet SOUNDS = SoundEvents.CAT_SOUNDS.get(CatSoundVariants.SoundSet.CLASSIC).adultSounds();

    public UndeadCat(EntityType<? extends UndeadCat> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createUndeadAnimalAttributes(12.0, 0.3, 3.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getTarget() != null ? SOUNDS.hissSound().value() : SOUNDS.strayAmbientSound().value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SOUNDS.hurtSound().value();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SOUNDS.deathSound().value();
    }
}
