package ai;

/**
 *
 * @author Matt
 */
public class PathInfo {
    private final int fromX;
    private final int fromY;
    private final int toX;
    private final int toY;
    private final double dist;
    public PathInfo(int startX, int startY, int endX, int endY, double d){
        fromX = startX;
        fromY = startY;
        toX = endX;
        toY = endY;
        dist = d;
    }
    
    public int getStartX(){
        return fromX;
    }
    public int getStartY(){
        return fromY;
    }
    public int getEndX(){
        return toX;
    }
    public int getEndY(){
        return toY;
    }
    
    public double getDist(){
        return dist;
    }
    
    @Override
    public String toString(){
        return String.format("(%d, %d) to (%d, %d): %f", fromX, fromY, toX, toY, dist);
    }
}
