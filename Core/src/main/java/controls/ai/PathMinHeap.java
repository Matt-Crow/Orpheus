package controls.ai;

import java.util.PriorityQueue;

/**
 *
 * @author Matt
 */
public class PathMinHeap {
    private final PriorityQueue<PathInfo> queue;
    
    public PathMinHeap(int size){
        queue = new PriorityQueue<>(size, (a, b) -> {
            if (a.getAccumDist() < b.getAccumDist()) {
                return -1;
            } else if (a.getAccumDist() > b.getAccumDist()) {
                return 1;
            }
            return 0;
        });
    }
    
    public void siftUp(PathInfo p) {
        queue.add(p);
    }
    
    public PathInfo siftDown() throws Exception{
        var shortest = queue.poll();
        if (shortest == null) {
            throw new Exception("heap is empty");
        }
        return shortest;
    }
}
