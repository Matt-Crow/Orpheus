package orpheus.core.world.occupants.players.attributes.requirements;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Requirements which must be met before an attribute can be triggered.
 */
public class ActivationRequirements {
    
    private final List<ActivationRequirement> activationRequirements;

    public ActivationRequirements(List<ActivationRequirement> activationRequirements) {
        this.activationRequirements = activationRequirements;
    }

    public List<ActivationRequirement> toList() {
        return List.copyOf(activationRequirements);
    }

    public void add(ActivationRequirement requirement) {
        activationRequirements.add(requirement);
    }

    public boolean areMet() {
        return activationRequirements.stream().allMatch(ActivationRequirement::isMet);
    }

    public List<String> getUnavailabilityMessages() {
        var messages = activationRequirements
            .stream()
            .map(ActivationRequirement::getUnavailabilityMessage)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        return messages;
    }
}
