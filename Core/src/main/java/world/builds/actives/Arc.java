package world.builds.actives;

/**
 * Determines how an arc of projectiles is generated
 */
public enum Arc {

    /**
     * Do not generate an arc - just do one projectile
     */
    NONE(0),

    NARROW(45),
    NORMAL(90),
    WIDE(135),
    CIRCULAR(360);

    private final int degrees;

    private Arc(int degrees) {
        this.degrees = degrees;
    }

    public int getDegrees() {
        return degrees;
    }
}
