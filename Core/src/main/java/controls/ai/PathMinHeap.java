package controls.ai;

import java.io.Serializable;
import static java.lang.System.out;

/**
 *
 * @author Matt
 */
public class PathMinHeap implements Serializable{
    private final int maxSize;
    private final PathInfo[] heap;
    private int firstEmptyIdx;
    
    public PathMinHeap(int size){
        maxSize = size;
        heap = new PathInfo[size];
        firstEmptyIdx = 0;
    }
    
    public void siftUp(PathInfo p) throws Exception{
        if(firstEmptyIdx >= maxSize){
            throw new Exception("Heap is full");
        }
        heap[firstEmptyIdx] = p;
        int currIdx = firstEmptyIdx;
        int parentIdx = (firstEmptyIdx - 1) / 2;
        //since heaps are binary trees, each child is 2i + 1 and 2i + 2,
        //subtracting one yields 2i and 2i + 1, and integer division yields i
        firstEmptyIdx++;
        PathInfo temp = null;
        
        //out.println("Sifting up...");
        //print();
        while(parentIdx > 0 && heap[parentIdx].getAccumDist() > p.getAccumDist()){
            //want the smallest on top
            temp = heap[parentIdx];
            heap[parentIdx] = p;
            heap[currIdx] = temp;
            currIdx = parentIdx;
            parentIdx = (currIdx - 1) / 2;
            //print();
        }
        //out.println("Done sifting up");
    }
    
    public PathInfo siftDown() throws Exception{
        if(firstEmptyIdx == 0){
            throw new Exception("Nothing to sift down");
        }
        PathInfo ret = heap[0];
        firstEmptyIdx--;
        heap[0] = heap[firstEmptyIdx]; //the last element becomes the first
        int currIdx = 0;
        int left = 1;
        int right = 2;
        PathInfo temp = null;
        //out.println("Sifting down...");
        //print();
        while(
            (left < firstEmptyIdx && heap[currIdx].getAccumDist() > heap[left].getAccumDist()) 
            || (right < firstEmptyIdx && heap[currIdx].getAccumDist() > heap[right].getAccumDist()))
        {
            if(heap[left].getAccumDist() > heap[right].getAccumDist()){
                temp = heap[right];
                heap[right] = heap[currIdx];
                heap[currIdx] = temp;
                currIdx = right;
            } else {
                temp = heap[left];
                heap[left] = heap[currIdx];
                heap[currIdx] = temp;
                currIdx = left;
            }
            left = 2 * currIdx + 1;
            right = 2 * currIdx + 2;
            //print();
        }
        //out.println("Done sifting down");
        return ret;
    }
    
    public void print(){
        int row = 0;
        int rowWidth = 1;
        for(int i = 0; i < firstEmptyIdx; i++){
            out.print(heap[i]);
            out.print(" | ");
            row++;
            if(row == rowWidth){
                rowWidth *= 2;
                row = 0;
                out.println();
            }
        }
        out.println();
    }
}
