package util;

import java.io.*;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * I will likely remove this class later
 * 
 * Provides a variation of a LinkedList that is immune to
 * concurrent modification exceptions.
 * 
 * One extremely useful thing this class does is automatically 
 * remove Nodes as their data terminates
 * @see Node
 * @see actions.Terminable
 * 
 * @author Matt Crow
 * @param <T> the type of element to include in this list
 */
public class SafeList<T> implements Serializable{
    private volatile Node<T> head;
    private volatile Node<T> tail;
    private volatile boolean isIterating;
    
    public SafeList(){
        head = null;
        tail = null;
        
        isIterating = false;
    }
    
    public Node<T> getHead(){
        return head;
    }
    
    public synchronized void headIsDead(){
        if(head == null){
            return;
        }
        if(head.hasChild()){
            head = head.getNext();
            head.setPrev(null);
        } else {
            //no more nodes
            head = null;
            tail = null;
        }
    }
    public synchronized void tailFailed(){
        if(tail == null){
           return;
       }
       if(tail.hasParent()){
           tail = tail.getPrev();
           tail.setNext(null);
       } else {
           //no more nodes
           head = null;
           tail = null;
       }
    }
    
    /**
     * Inserts a given value at the front of
     * the list. That way, if this is being
     * iterated over, but add is called, it 
     * will not alter the iteration: Yay no concmod.
     * @param val the value to add
     * @return the node which now contains val
     */
    public synchronized Node<T> add(T val){
        Node<T> nn;
        if(head == null){
            nn = new Node<>(this, val);
            head = nn;
            tail = nn;
        } else {
            nn = head.insert(val);
        }
        return nn;
    }
    public void clear(){
        head = null;
        tail = null;
    }
    public synchronized void setHead(Node<T> nn){
        if(nn == null){
            throw new NullPointerException();
        }
        Node<T> curr = nn;
        while(curr.hasParent()){
            curr = curr.getPrev();
        }
        head = curr;
    }
    
    public synchronized boolean remove(T val){
        boolean wasRemoved = false;
        
        Node<T> curr = head;
        while(curr != null && !wasRemoved){
            if(curr.getValue().equals(val)){
                wasRemoved = true;
                curr.delete();
            }else{
                curr = curr.getNext();
            }
        }
        return wasRemoved;
    }
    
    public void forEach(Consumer<T> action) {
        boolean alreadyIterating = isIterating;
        //this way, a forEach inside a forEach doesn't reset isIterating
        
        isIterating = true;
        Node<T> curr = head;
        while(curr != null){
            action.accept(curr.getValue());
            curr = curr.getNext();
        }
        if(!alreadyIterating){
            //System.out.println("break");
            //is iterating will only be set to false for the outermost use of forEach
            isIterating = false;
        }else{
            //System.out.println("don't break");
        }
    }
    
    public Object[] toArray(){
        synchronized(this){
            //cannot do generic array
            int len = length();
            Object[] ret = new Object[len];
            Node<T> curr = head;
            for(int i = 0; i < len; i++){
                ret[i] = curr.getValue();
                curr = curr.getNext();
            }
            return ret;
        }
    }
    
    public void displayData(){
        System.out.println("SAFE LIST:");
        forEach((T val)->{
            System.out.println("* " + val.toString());
        });
        System.out.println("END OF SAFE LIST");
    }
    
    public int length(){
        int ret = 0;
        Node<T> curr = head;
        while(curr != null){
            ret++;
            curr = curr.getNext();
        }
        return ret;
    }
    
    private void writeObject(ObjectOutputStream oos) throws IOException{
        oos.writeBoolean(isIterating);
        oos.writeInt(length()); //how many elements are in the list that should be serialized
        Node<T> curr = head;
        while(curr != null){
            oos.writeObject(curr);
            curr = curr.getNext();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        isIterating = ois.readBoolean();
        int nodesRem = ois.readInt(); //how many nodes there are left to read
        Node<T> prev = null;
        Node<T> curr = null;
        while(nodesRem != 0){
            prev = curr;
            try {
                // this was occasionally throwing errors
                curr = (Node<T>)ois.readObject();
            } catch(Exception ex){
                throw ex;
            }
            if(prev == null){
                head = curr;
            } else {
                prev.setNext(curr);
            }
            --nodesRem;
        }
    }
    
    public static void main(String[] args){
        SafeList<Integer> ll = new SafeList<>();
        ll.add(1);
        ll.add(2);
        ll.add(3);
        
        ll.forEach((Integer i)->{
            System.out.println(i);
            //ll.add(i * i);
            ll.remove(i + 1);
            ll.add(i + 2);
            ll.displayData();
        });
        ll.displayData();
        System.out.println(Arrays.toString(ll.toArray()));
        System.out.println(ll.isIterating);
    }
}
