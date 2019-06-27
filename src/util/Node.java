package util;

/**
 * The Node class is used by
 * SafeList to contain items
 * in the list. Functions exactly
 * as a linked list node.
 * 
 * @author Matt Crow
 * @param <T> the type of element this Node will contain
 */
public class Node<T> {
    private Node<T> next;
    private Node<T> prev;
    private final T val;
    
    public Node(T value){
        val = value;
    }
    
    public T getValue(){
        return val;
    }
    
    public void setPrev(Node<T> n){
        prev = n;
    }
    public Node<T> getPrev(){
        return prev;
    }
    
    public void setNext(Node<T> n){
        next = n;
    }
    public Node<T> getNext(){
        return next;
    }
}
