package com.nyfaria.undeadtakeover.entity.ai.goals;

import com.nyfaria.undeadtakeover.entity.BloodSipper;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class BiteGoal extends MeleeAttackGoal {
    private final BloodSipper sipper;

    public BiteGoal(BloodSipper sipper) {
        super(sipper, 1.4, true);
        this.sipper = sipper;
    }

    @Override
    public boolean canUse() {
        return this.sipper.canBite() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.sipper.canBite() && super.canContinueToUse();
    }
}
