package orpheus.core.champions.orpheus;

import java.util.Optional;

import util.Settings;
import world.builds.passives.AbstractPassive;

public class ScrapMetal extends AbstractPassive {

    /**
     * the instance of the Orpheus champion this is attached to
     */
    private Optional<OrpheusChampion> orpheus = Optional.empty();

    /**
     * used when rendering
     */
    private double angleOffset = 0.0;

    private static final double RATE = 360.0 / Settings.seconds(3.0);

    public ScrapMetal(OrpheusChampion orpheus) {
        this();
        setOrpheus(orpheus);
    }

    public ScrapMetal() {
        super("Scrap Metal", true);
    }

    public void setOrpheus(OrpheusChampion orpheus) {
        this.orpheus = Optional.of(orpheus);
    }

    /**
     * Used by a unit test.
     * @return whether an instance of OrpheusChampion is attached to this.
     */
    protected boolean isOrpheusSet() {
        return orpheus.isPresent();
    }

    protected double getAngleOffset() {
        return angleOffset;
    }

    @Override
    public void init() {
        super.init();
        withUser(user -> {
            user.eventOnKill()
                .add(e -> trigger());
            user.eventOnTakeDamage()
                .add(e -> {
                    if (e.getDamagePercent() > 0.2) {
                        trigger();
                    }
                });
            user.eventOnUpdate().add((e) -> {
                    angleOffset = (angleOffset + RATE) % 360;
                });
        });
    }

    private void trigger() {
        orpheus.ifPresent(OrpheusChampion::gainScrapMetal);
    }

    @Override
    public ScrapMetal copy() {
        return new ScrapMetal();
    }

    @Override
    public String getDescription() {
        return "Whenever Orpheus vanquishes an enemy or loses 20% of his health in a single hit, he gains a piece of scrap metal.";    
    }
}
