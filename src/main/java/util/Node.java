package util;

import events.Terminable;
import events.TerminateListener;
import java.io.Serializable;

/**
 * The Node class is used by
 * SafeList to contain items
 * in the list. Functions exactly
 * as a linked list node, except this
 * is thread safe, and immune to concurrent
 * modification exceptions.
 * 
 * @author Matt Crow
 * @param <T> the type of element this Node will contain
 */
public class Node<T> implements Serializable, TerminateListener{
    private final SafeList<T> container; 
    private transient volatile Node<T> prev;
    private transient volatile Node<T> next;
    private final T val;
    
    /**
     * Stores the given value in this Node.
     * If the given value implements Terminable,
     * once that Object runs all of its termination
     * listeners, this will delete itself from its parent.
     * 
     * @param parent the SafeList containing this Node.
     * @param value the value this Node should contain.
     */
    public Node(SafeList<T> parent, T value){
        if(parent == null || value == null){
            throw new NullPointerException();
        }
        if(value instanceof Terminable){
            ((Terminable)value).addTerminationListener(this);
        }
        container = parent;
        val = value;
        next = null;
        prev = null;
    }
    
    public T getValue(){
        return val;
    }
    
    public SafeList<T> getContainer(){
        return container;
    }
    
    public boolean hasParent(){
        return prev != null;
    }
    
    public boolean hasChild(){
        return next != null;
    }
    
    /**
     * Inserts a node before this one.
     * If this already has a previous node,
     * sandwiches the new node in between this
     * and this' previous
     * @param n the node to insert
     */
    public synchronized void setPrev(Node<T> n){
        if(n == null){
            prev = null;
            return;
        }
        if(n.hasParent() || n.hasChild()){
            throw new IllegalArgumentException("Cannot insert a Node which already has a parent or child");
        }
        if(prev != null){
            prev.next = n;
            n.prev = prev;
        }
        prev = n;
        n.next = this;
    }
    
    /**
     * Inserts a node after this one.
     * If this already has a next node,
     * sandwiches the new node in between this
     * and this' next
     * @param n the node to insert
     */
    public synchronized void setNext(Node<T> n){
        if(n == null){
            next = null;
            return;
        }
        if(n.hasParent() || n.hasChild()){
            throw new IllegalArgumentException("Cannot insert a Node which already has a parent or child");
        }
        if(next != null){
            next.prev = n;
            n.next = next;
        }
        next = n;
        n.prev =  this;
    }
    
    //not sure if I need these
    public synchronized Node<T> getPrev(){
        return prev;
    }
    public synchronized Node<T> getNext(){
        return next;
    }
    
    /**
     * Inserts a new Node in front of this one
     * @param data 
     * @return the Node containing data, after it has been inserted
     */
    public synchronized Node<T> insert(T data){
        if(data == null){
            throw new NullPointerException();
        }
        if(data == val){
            throw new IllegalArgumentException(data + " is trying to inserts itself");
        }
        
        Node<T> nn = new Node<>(container, data);
        if(prev == null){
            container.setHead(nn);
        } else {
            prev.next = nn;
            nn.prev = prev;
        }
        prev = nn;
        nn.next = this;
        return nn;
    }
    
    public synchronized void delete(){
        if(prev == null){
            container.headIsDead();
            if(next != null){
                next.prev = null; 
            }
        } else {
            prev.next = next;
        }
        
        if(next == null){
            container.tailFailed();
            if(prev != null){
                prev.next = null;
            }
        } else {
            next.prev = prev;
        }
    }

    @Override
    public void objectWasTerminated(Object o) {
        if(o == val){
            delete();
            if(o instanceof Terminable){
                ((Terminable)o).removeTerminationListener(this);
            }
        }
    }
}
