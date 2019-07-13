package ai;

import java.io.Serializable;

/**
 * The PathInfo class is used to record paths through a World.
 * It functions more as a struct than a class, mostly just used to store data
 * as opposed to actually doing anything with it.
 * 
 * @author Matt Crow
 */
public class PathInfo implements Serializable{
    private final int fromX;
    private final int fromY;
    private final int toX;
    private final int toY;
    private final double accumDist;
    private final double dist;
    
    /**
     * 
     * @param startX the X coordinate of the start of the path
     * @param startY 
     * @param endX
     * @param endY
     * @param d the ACCUMULATED distance to this point. Used by Dijkstra's algorithm
     */
    public PathInfo(int startX, int startY, int endX, int endY, double d){
        fromX = startX;
        fromY = startY;
        toX = endX;
        toY = endY;
        accumDist = d;
        dist = Math.sqrt(Math.pow(fromX - toX, 2) + Math.pow(fromY - toY, 2));
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
    
    public double getAccumDist(){
        return accumDist;
    }
    public double getDist(){
        return dist;
    }
    
    @Override
    public String toString(){
        return String.format("(%d, %d) to (%d, %d): Distance: %f, Accumulated: %f", fromX, fromY, toX, toY, dist, accumDist);
    }
}
