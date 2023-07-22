package orpheus.core.world.occupants.players.attributes.requirements;

import java.util.Optional;

/**
 * a condition which must be met for a player to trigger an attribute
 */
public interface ActivationRequirement {
    
    /**
     * @return whether the activation requirement is met, and thus the attribute
     *  can be triggered
     */
    public boolean isMet();

    /**
     * If the activation requirement is not met, returns a message detailing why
     * @return perhaps a message detailing why the attribute cannot be triggered
     */
    public Optional<String> getUnavailabilityMessage();
}
