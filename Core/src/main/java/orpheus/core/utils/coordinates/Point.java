package orpheus.core.utils.coordinates;

public class Point {
    
    private double x;
    private double y;

    public Point() {
        this(0, 0);
    }
    
    public Point(double x, double y) {
        setX(x);
        setY(y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void addX(double dx) {
        setX(getX() + dx);
    }

    public void addY(double dy) {
        setY(getY() + dy);
    }

    public void add(Vector velocity) {
        addX(velocity.getVelocityX());
        addY(velocity.getVelocityY());
    }

    public Point copy() {
        return new Point(getX(), getY());
    }
}
