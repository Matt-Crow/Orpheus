package world.builds.actives;

public enum Damage {
    NONE(0.0),
    LOW(0.05),
    MEDIUM(0.1),
    HIGH(0.2);

    private final double percentage;

    private Damage(double percentage) {
        if (percentage > 1.0) {
            throw new IllegalArgumentException("percentage should be between 0 and 1");
        }
        this.percentage = percentage;
    }

    /**
     * the percentage of an average player's health this represents
     * @return a number between 0 and 1
     */
    public double getPercentage() {
        return percentage;
    }
}
