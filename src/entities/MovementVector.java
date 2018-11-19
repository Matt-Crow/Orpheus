package entities;



public final class MovementVector {
    private final int x; //total movement in x direction
    private final int y;
    private final int duration; //in frames. Move to outside?
    private int timeIn;
    
    public MovementVector(int xComp, int yComp, int dur){
        x = xComp;
        y = yComp;
        duration = dur;
        timeIn = 0;
    }
    public MovementVector(int xComp, int yComp){
        this(xComp, yComp, 1);
    }
    
    public int getX(){
        return (int)((double)x / duration);
    }
    public int getY(){
        return (int)((double)y / duration);
    }
}
