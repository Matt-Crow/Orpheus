package orpheus.core.utils.coordinates;

import java.util.function.Supplier;

import util.Direction;
import world.builds.actives.Arc;

public class RotatingTerminalPointUpdater implements TerminablePointUpdater {

    private final double radius;
    private final Direction startingAngle;
    private final Arc arc;
    private final double dTheta;
    private final Supplier<Point> center;
    private double rotatedThusFar;

    public RotatingTerminalPointUpdater(double radius, Direction startingAngle, Arc arc, int frames, Supplier<Point> center) {
        this.radius = radius;
        this.startingAngle = startingAngle;
        this.arc = arc;
        this.dTheta = (double)arc.getDegrees() / frames;
        this.center = center;
        rotatedThusFar = 0.0;
    }
    
    public void update(Point point) {
        var theta = startingAngle.rotatedBy((int)rotatedThusFar);
        var newX = center.get().getX() + radius * theta.getXMod();
        var newY = center.get().getY() + radius * theta.getYMod();
        rotatedThusFar += dTheta;
        point.setX(newX);
        point.setY(newY);
    }

    @Override
    public boolean isDone() {
        return arc.getDegrees() <= rotatedThusFar;
    }
}
