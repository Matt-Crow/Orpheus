package orpheus.core.world.occupants.players.attributes.requirements;

import java.util.Optional;
import java.util.function.Supplier;

public class SimpleActivationRequirement implements ActivationRequirement {

    private final Supplier<Boolean> met;
    private final Supplier<Optional<String>> unavailabilityMessage;

    public SimpleActivationRequirement(
        Supplier<Boolean> met, 
        Supplier<Optional<String>> unavailabilityMessage
    ) {
        this.met = met;
        this.unavailabilityMessage = unavailabilityMessage;
    }
    @Override
    public boolean isMet() {
        return met.get();
    }

    @Override
    public Optional<String> getUnavailabilityMessage() {
        return unavailabilityMessage.get();
    }
}
