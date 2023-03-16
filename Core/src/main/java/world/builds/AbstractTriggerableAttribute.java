package world.builds;

import util.Settings;
import world.entities.AbstractPlayer;
import world.statuses.AbstractStatus;
import world.statuses.StatusTable;

/**
 * A AbstractTriggerableAttribute is a AbstractBuildAttribute that can be
 * triggered - either actively or passively.
 *
 * @author Matt Crow
 */
public abstract class AbstractTriggerableAttribute extends AbstractBuildAttribute {

    private int cooldownTime;          // frames between uses of this upgradable in battle
    private int framesUntilUse;        // frames until this upgradable can be used in battle again

    private final StatusTable inflict;

    public AbstractTriggerableAttribute(String n) {
        super(n);

        cooldownTime = 0;
        framesUntilUse = 0;

        inflict = new StatusTable();
    }

    /**
     * Sets the maximum frequency of how often this can be used. Each subclass
     * must still deal with this in their own way.
     *
     * @param seconds the minimum number of seconds between each use of this.
     */
    public final void setCooldownTime(int seconds) {
        cooldownTime = Settings.seconds(seconds);
    }

    /**
     * Gets how long until this can be used again
     *
     * @return how many frames until this is considered "off cooldown"
     */
    public final int getFramesUntilUse() {
        return framesUntilUse;
    }

    /**
     * Notify this upgradable that it has been used.
     */
    public final void setToCooldown() {
        framesUntilUse = cooldownTime;
    }

    /**
     * Gets if this should be usable.
     *
     * @return whether or not this is "on cooldown"
     */
    public final boolean isOnCooldown() {
        return framesUntilUse > 0;
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
    public final void applyEffect(AbstractPlayer p) {
        inflict.forEach((status) -> {
            p.inflict(status.copy());
        });
    }

    @Override
    public void init() {
        framesUntilUse = 0;
    }

    @Override
    public void update() {
        if (framesUntilUse > 0) {
            framesUntilUse -= 1;
        }
    }

    /**
     * This method should be invoked by subclasses
     */
    public abstract void trigger();
}
