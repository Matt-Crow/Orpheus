package world.events;

/**
 * Created whenever damage is first inflicted, not when it is applied.
 * Keep in mind that the damage backlog causes a delay between when damage is
 * inflicted and applied, which explains this discrepency.
 */
public class DamageEvent implements Event {
    
    private final int damageInflicted;
    private final double damagePercent;

    public DamageEvent(int damageInflicted, double damagePercent) {
        this.damageInflicted = damageInflicted;
        this.damagePercent = damagePercent;
    }

    public int getDamageInflicted() {
        return damageInflicted;
    }

    public double getDamagePercent() {
        return damagePercent;
    }
}
