package orpheus.core.utils.coordinates;

import util.Direction;

public class PolarVector implements Vector {

    private double speed;
    private Direction angle;

    public PolarVector(double speed, Direction angle) {
        this.speed = speed;
        this.angle = angle;
    }

    @Override
    public double getVelocityX() {
        return speed * angle.getXMod();
    }

    @Override
    public double getVelocityY() {
        return speed * angle.getYMod();
    }
}
