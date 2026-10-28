package com.nyfaria.undeadtakeover.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class UndeadSheep extends UndeadAnimal implements Shearable {
    private static final EntityDataAccessor<Boolean> DATA_SHEARED = SynchedEntityData.defineId(UndeadSheep.class, EntityDataSerializers.BOOLEAN);

    public UndeadSheep(EntityType<? extends UndeadSheep> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createUndeadAnimalAttributes(16.0, 0.23, 3.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_SHEARED, false);
    }

    public boolean isSheared() {
        return this.entityData.get(DATA_SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.entityData.set(DATA_SHEARED, sheared);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.SHEARS)) {
            if (this.level() instanceof ServerLevel level && this.readyForShearing()) {
                this.shear(level, SoundSource.PLAYERS, stack);
                this.gameEvent(GameEvent.SHEAR, player);
                stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
                return InteractionResult.SUCCESS_SERVER;
            }

            return InteractionResult.CONSUME;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
        level.playSound(null, this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);
        this.setSheared(true);
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Sheared", this.isSheared());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setSheared(input.getBooleanOr("Sheared", false));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }
}
