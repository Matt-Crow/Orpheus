package world.builds.actives;

public class Damage {
    public static final Damage NONE = new Damage(0.0);
    public static final Damage LOW = new Damage(0.05);
    public static final Damage MEDIUM = new Damage(0.1);
    public static final Damage HIGH = new Damage(0.2);

    private final double percentage;

    private Damage(double percentage) {
        if (percentage > 1.0) {
            throw new IllegalArgumentException("percentage should be between 0 and 1");
        }
        this.percentage = percentage;
    }

    public static Damage percent(double percentage) {
        return new Damage(percentage);
    }

    /**
     * the percentage of an average player's health this represents
     * @return a number between 0 and 1
     */
    public double getPercentage() {
        return percentage;
    }
}
