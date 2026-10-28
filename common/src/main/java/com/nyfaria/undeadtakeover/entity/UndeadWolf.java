package com.nyfaria.undeadtakeover.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class UndeadWolf extends UndeadAnimal {
    private static final WolfSoundVariant.WolfSoundSet SOUNDS = SoundEvents.WOLF_SOUNDS.get(WolfSoundVariants.SoundSet.ANGRY).adultSounds();

    public UndeadWolf(EntityType<? extends UndeadWolf> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createUndeadAnimalAttributes(20.0, 0.3, 4.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SOUNDS.growlSound().value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SOUNDS.hurtSound().value();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SOUNDS.deathSound().value();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SOUNDS.stepSound().value(), 0.15F, 1.0F);
    }
}
