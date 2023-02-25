package world.entities;

import java.io.Serializable;

import orpheus.core.world.graph.Graphable;
import util.Direction;

/**
 * confusingly, AbstractPrimitiveEntity is the superclass of AbstractEntity,
 * which has a less-specific name. This class encapsulates the minimum set of
 * attributes required by something that moves
 *
 * @author Matt Crow
 */
public abstract class AbstractPrimitiveEntity implements Serializable {//, Graphable {

    private int x;
    private int y;
    private int radius;
    private int maxSpeed;
    private boolean isMoving;
    private Direction facing;

    public AbstractPrimitiveEntity() {
        x = 0;
        y = 0;
        radius = 50;
        maxSpeed = 0;
        isMoving = false;
        facing = new Direction(0);
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final void setX(int xc) {
        x = xc;
    }

    public final void setY(int yc) {
        y = yc;
    }

    public final void setRadius(int r) {
        if (r < 0) {
            throw new IllegalArgumentException(String.format("radius must be non-negative, so %d isn't allowed", r));
        }
        radius = r;
    }

    public final int getRadius() {
        return radius;
    }

    public final void setMaxSpeed(int speed) {
        maxSpeed = speed;
    }

    public final int getMaxSpeed() {
        return maxSpeed;
    }

    public final void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public final boolean getIsMoving() {
        return isMoving;
    }

    public final void setFacing(int degrees) {
        facing.setDegrees(degrees);
    }

    public final Direction getFacing() {
        return facing;
    }

    public final void turnTo(int xCoord, int yCoord) {
        facing = Direction.getDegreeByLengths(
                getX(),
                getY(),
                xCoord,
                yCoord
        );
    }

    protected int getMomentum() {
        return maxSpeed;
    }

    public final boolean isWithin(int x, int y, int w, int h) {
        return (x < getX() + radius //left
                && x + w > getX() - radius //right
                && y < getY() + radius //top
                && y + h > getY() - radius //bottom
                );
    }

    /**
     * can be overridden, but subclasses should ensure they call super.update()
     * in their implementation.
     */
    public void update() {
        updateMovement();
    }

    /**
     * can be overridden, but subclasses should ensure they call
     * super.updateMovement() in their implementation.
     */
    protected void updateMovement() {
        if (isMoving) {
            x += getMomentum() * facing.getXMod();
            y += getMomentum() * facing.getYMod();
        }
    }

    public abstract void init();

    /*
    @Override
    public orpheus.core.world.graph.Entity toGraph() {
        return new orpheus.core.world.graph.Entity(x, y, radius);
    }*/
}
