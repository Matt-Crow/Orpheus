package util;

/**
 * The Node class is used by
 * SafeList to contain items
 * in the list. Functions exactly
 * as a linked list node, except this
 * is thread safe, and immune to concurrent
 * modification exceptions
 * 
 * @author Matt Crow
 * @param <T> the type of element this Node will contain
 */
public class Node<T> {
    private final SafeList<T> container; 
    private volatile Node<T> next;
    private volatile Node<T> prev;
    private final T val;
    
    /**
     * 
     * @param parent the SafeList containing this Node.
     * @param value the value this Node should contain.
     */
    public Node(SafeList<T> parent, T value){
        if(parent == null || value == null){
            throw new NullPointerException();
        }
        container = parent;
        val = value;
        next = null;
        prev = null;
    }
    
    public T getValue(){
        return val;
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
            throw new NullPointerException();
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
            throw new NullPointerException();
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
     */
    //nd
    public synchronized void insert(T data){
        if(data == null){
            throw new NullPointerException();
        }
        if(data.equals(val)){
            throw new IllegalArgumentException(data + " is trying to inserts itself");
        }
        
        Node<T> nn = new Node<>(container, data);
        if(prev == null){
            container.setHead(nn);
        } else {
            prev.next = nn;
            nn.prev = next;
        }
        prev = nn;
        nn.next = this;
    }
}
