package util;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Provides a variation of a LinkedList that is immune to
 * concurrent modification exceptions.
 * 
 * I may also use this class for some of the methods of Player and Entity
 * (managing statuses, action registers, teams, etc)
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
     */
    public synchronized void add(T val){
        if(head == null){
            Node<T> nn = new Node<>(this, val);
            head = nn;
            tail = nn;
        } else {
            head.insert(val);
        }
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
        System.out.println(ll.isIterating);
    }
}
