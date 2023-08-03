package orpheus.core.world.occupants.players.attributes.requirements;

import java.util.Optional;

import util.Settings;

/**
 * requires that an attribute not be triggered to close to the last time it
 * triggered
 */
public class CooldownRequirement implements ActivationRequirement {

    private final int cooldownInFrames;
    private int framesUntilUse = 0;

    public CooldownRequirement(int cooldownInFrames) {
        this.cooldownInFrames = cooldownInFrames;
    }

    public void setToCooldown() {
        framesUntilUse = cooldownInFrames;
    }

    public void init() {
        framesUntilUse = 0;
    }

    public void update() {
        if (framesUntilUse > 0) {
            framesUntilUse--;
        }    
    }

    @Override
    public boolean isMet() {
        return framesUntilUse == 0;
    }

    @Override
    public Optional<String> getUnavailabilityMessage() {
        return (isMet())
            ? Optional.empty()
            : Optional.of(String.format("On cooldown %4.2fseconds", Settings.framesToSeconds(framesUntilUse)));
    }

    @Override
    public CooldownRequirement copy() {
        return new CooldownRequirement(this.cooldownInFrames);
    }
}
