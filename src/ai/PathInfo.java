package ai;

/**
 *
 * @author Matt
 */
public class PathInfo {
    private final int from;
    private final int to;
    private final double dist;
    public PathInfo(int start, int end, double d){
        from = start;
        to = end;
        dist = d;
    }
}
