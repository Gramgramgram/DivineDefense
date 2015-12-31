package ua.gram.model.actor.enemy;

import ua.gram.DDGame;
import ua.gram.model.prototype.EnemyPrototype;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class AbilityUser extends Enemy implements AbilityUserInterface {

    protected final float abilityDelay;
    protected final float abilityDuration;
    protected float abilityDurationCount;
    protected float delayAbility;
    protected boolean isAbilityExecuted;
    protected boolean isInterrupted;

    public AbilityUser(DDGame game, EnemyPrototype prototype) {
        super(game, prototype);
        abilityDelay = prototype.abilityDelay;
        abilityDuration = prototype.abilityDuration;
        isAbilityExecuted = false;
        isInterrupted = false;
    }

    @Override
    public float getAbilityDelay() {
        return abilityDelay;
    }

    public void setAbilityDelay(float delayAbility) {
        this.delayAbility = delayAbility;
    }

    @Override
    public float getAbilityDuration() {
        return abilityDuration;
    }

    @Override
    public boolean isAbilityPossible(float delta) {
        if (delayAbility >= this.getAbilityDelay()) {
            delayAbility = 0;
            return true;
        } else {
            delayAbility += delta;
            return false;
        }
    }

    @Override
    public boolean isAbilityPossible() {
        if (delayAbility >= abilityDelay) {
            delayAbility = 0;
            return true;
        } else {
            delayAbility += 1;
            return false;
        }
    }

    @Override
    public boolean isAbilityExecuted() {
        return isAbilityExecuted;
    }

    @Override
    public void setAbilityExecuted(boolean executed) {
        this.isAbilityExecuted = executed;
    }

    public boolean isAbilityActive() {
        return abilityDurationCount > 0;
    }

    @Override
    public boolean isInterrupted() {
        return isInterrupted;
    }

    @Override
    public void reset() {
        super.reset();
        isInterrupted = false;
        isAbilityExecuted = false;
        abilityDurationCount = 0;
    }

    @Override
    public float getAbilityDurationCount() {
        return abilityDurationCount;
    }

    @Override
    public void setAbilityDurationCount(float duration) {
        this.abilityDurationCount = duration;
    }

    @Override
    public void addAbilityDurationCount(float duration) {
        this.abilityDurationCount += duration;
    }
}