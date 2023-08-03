package orpheus.core.utils.coordinates;

public class TerminableVectorPointUpdater extends VectorPointUpdater implements TerminablePointUpdater {

    private final double range;
    private double distanceTraveled = 0.0;

    public TerminableVectorPointUpdater(Vector velocity, double range) {
        super(velocity);
        this.range = range;
    }

    @Override
    public boolean isDone() {
        return distanceTraveled >= range;
    }
    
    @Override
    public void update(Point point) {
        if (!isDone()) {
            super.update(point);
            distanceTraveled += getVelocity().getSpeed();
        }
    }
}
