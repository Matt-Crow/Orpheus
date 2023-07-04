package orpheus.core.utils.coordinates;

public class VectorPointUpdater implements PointUpdater {
    
    private final Vector velocity;

    public VectorPointUpdater(Vector velocity) {
        this.velocity = velocity;
    }

    protected Vector getVelocity() {
        return velocity;
    }

    @Override
    public void update(Point point) {
        point.add(velocity);
    }
}
