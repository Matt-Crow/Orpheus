package entities;
import resources.Direction;


public final class MovementVector {
    private final int x; //total movement in x direction
    private final int y;
    private final int duration; //in frames
    
    public MovementVector(int xComp, int yComp, int dur){
        x = xComp;
        y = yComp;
        duration = dur;
    }
    public MovementVector(int xComp, int yComp){
        this(xComp, yComp, 1);
    }
    public MovementVector(int r, Direction d, int dur){
        //polar
        this((int)(d.getXMod() * r), (int)(d.getYMod() * r), dur);
    }
    public MovementVector(int r, Direction d){
        this(r, d, 1);
    }
    
    public int getX(){
        return (int)((double)x / duration);
    }
    public int getY(){
        return (int)((double)y / duration);
    }
    public int getDur(){
        return duration;
    }
    
    @Override
    public String toString(){
        return "Movement Vector: X: " + getX() + ", Y: " + getY() + ", Duration: " + getDur();
    }
}
