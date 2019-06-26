/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Matt
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
