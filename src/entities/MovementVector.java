package entities;
import resources.Coordinates;
import resources.Direction;


public final class MovementVector {
    private final Direction dir;
    private final int magnitude;
    private final int duration; // in frames
    
    public MovementVector(int mag, int degrees, int dur){
        dir = new Direction(degrees);
        magnitude = mag;
        duration = dur;
    }
    public MovementVector(int mag, int degrees){
        this(mag, degrees, 1);
    }
    public MovementVector(Entity from, Entity to){
        this((int)Coordinates.distanceBetween(from, to), Direction.getDegreeByLengths(from.getX(), from.getY(), to.getX(), to.getY()).getDegrees());
    }
    
    public int getX(){
        return (int)(dir.getXMod() * magnitude);
    }
    public int getY(){
        return (int)(dir.getYMod() * magnitude);
    }
    public int getDur(){
        return duration;
    }
    
    @Override
    public String toString(){
        return "Movement Vector: X: " + getX() + ", Y: " + getY() + ", Duration: " + getDur();
    }
}
