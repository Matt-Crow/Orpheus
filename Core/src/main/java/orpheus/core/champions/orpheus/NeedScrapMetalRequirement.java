package orpheus.core.champions.orpheus;

import java.util.Optional;

import orpheus.core.world.occupants.players.attributes.requirements.ActivationRequirement;

public class NeedScrapMetalRequirement implements ActivationRequirement {

    private final OrpheusChampion user;

    public NeedScrapMetalRequirement(OrpheusChampion user) {
        this.user = user;
    }

    @Override
    public boolean isMet() {
        return user.hasScrapMetal();
    }

    @Override
    public Optional<String> getUnavailabilityMessage() {
        return (isMet()) ? Optional.empty() : Optional.of("need scrap metal");
    }

    @Override
    public ActivationRequirement copy() {
        return this;
    }
    
}
