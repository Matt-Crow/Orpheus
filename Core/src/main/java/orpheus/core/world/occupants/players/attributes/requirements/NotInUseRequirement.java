package orpheus.core.world.occupants.players.attributes.requirements;

import java.util.Optional;

public class NotInUseRequirement implements ActivationRequirement {

    private final String unavailabilityMessage;
    private boolean inUse = false;


    public NotInUseRequirement(String unavailabilityMessage) {
        this.unavailabilityMessage = unavailabilityMessage;
    }

    public void use() {
        inUse = true;
    }

    public void doneUsing() {
        inUse = false;
    }

    @Override
    public boolean isMet() {
        return !inUse;
    }

    @Override
    public Optional<String> getUnavailabilityMessage() {
        return (inUse) ? Optional.of(unavailabilityMessage) : Optional.empty();
    }

    @Override
    public ActivationRequirement copy() {
        return new NotInUseRequirement(unavailabilityMessage);
    }
}
