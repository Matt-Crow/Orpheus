package world.builds;

import orpheus.core.world.occupants.players.Player;
import world.statuses.AbstractStatus;
import world.statuses.StatusTable;

/**
 * A AbstractTriggerableAttribute is a AbstractBuildAttribute that can be
 * triggered - either actively or passively.
 *
 * @author Matt Crow
 */
public abstract class AbstractTriggerableAttribute extends AbstractBuildAttribute {

    private final StatusTable inflict;

    public AbstractTriggerableAttribute(String n) {
        super(n);
        inflict = new StatusTable();
    }

    /**
     * Adds a copy of the given status to this inflict table.
     *
     * @param s
     */
    public final void addStatus(AbstractStatus s) {
        inflict.add(s);
    }

    /**
     * Adds copies of all the given statuses to this inflict table.
     *
     * @param ss
     */
    public final void addStatuses(AbstractStatus[] ss) {
        for (AbstractStatus s : ss) {
            inflict.add(s);
        }
    }

    /**
     * Used to get the table of statuses associated with this Customizable.
     *
     * @return
     */
    public final StatusTable getInflict() {
        return inflict;
    }

    /**
     * takes all the statuses from this upgradable's status table, and copies
     * them to a's
     *
     * @param a
     */
    public final void copyInflictTo(AbstractTriggerableAttribute a) {
        inflict.forEach((status) -> {
            a.addStatus(status);
        });
    }

    /**
     * Inflicts each status in this' status table on the given AbstractPlayer.
     *
     * @param p
     */
    public final void applyEffect(Player p) {
        inflict.forEach((status) -> {
            p.inflict(status.copy());
        });
    }

    /**
     * This method should be invoked by subclasses
     */
    public abstract void trigger();
}
