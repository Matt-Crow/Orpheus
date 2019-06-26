package util;

import java.util.function.Consumer;

/**
 * Provides a variation of a LinkedList that is immune to
 * concurrent modification exceptions.
 * 
 * @author Matt Crow
 * @param <T> the type of element to include in this list
 */
public class SafeList<T>{
    private Node<T> head;
    private Node<T> tail;
    
    private boolean isIterating;
    private SafeList<T> newNodes;
    
    public SafeList(){
        head = null;
        tail = null;
        
        isIterating = false;
        newNodes = null;
    }
    public SafeList(SafeList<T> sl){
        this();
        
        //do not call forEach here, as that will reset isIterating
        Node<T> curr = sl.head;
        while(curr != null){
            add(curr.getValue());
            curr = curr.getNext();
        }
    }
    
    public void add(T val){
        Node<T> nn = new Node<>(val);
        if(isIterating){
            if(newNodes == null){
                newNodes = new SafeList<>(this);
            }
            System.out.println(this + " add " + val);
            newNodes.add(val);
        }else{
            System.out.println(this + " add " + val);
            if(head == null){
                //no nodes
                head = nn;
                tail = nn;
            } else {
                tail.setNext(nn);
                nn.setPrev(tail);
                tail = nn;
            }
        }
    }
    
    //untested
    public boolean remove(T val){
        boolean wasRemoved = false;
        
        SafeList<T> removeFrom = this;
        if(isIterating){
            if(newNodes == null){
                newNodes = new SafeList<>(this);
            }
            removeFrom = newNodes;
        }
        
        Node<T> curr = removeFrom.head;
        while(curr != null && !wasRemoved){
            if(curr.getValue().equals(val)){
                wasRemoved = true;
                if(curr.getPrev() == null){
                    //is head
                    removeFrom.head = curr.getNext();
                } else {
                    curr.getPrev().setNext(curr.getNext());
                }
                
                if(curr.getNext() == null){
                    //is tail
                    removeFrom.tail = curr.getPrev();
                } else {
                    curr.getNext().setPrev(curr.getPrev());
                }
                
                if(removeFrom.head == null || removeFrom.tail == null){
                    //was only element
                    removeFrom.head = null;
                    removeFrom.tail = null;
                }
            }else{
                curr = curr.getNext();
            }
        }
        
        return wasRemoved;
    }
    
    public void forEach(Consumer<T> action) {
        isIterating = true;
        Node<T> curr = head;
        while(curr != null){
            action.accept(curr.getValue());
            curr = curr.getNext();
        }
        isIterating = false;
        if(newNodes != null){
            head = null;
            tail = null;
            
            //do not call forEach here, as that will reset isIterating
            curr = newNodes.head;
            while(curr != null){
                add(curr.getValue());
                curr = curr.getNext();
            }
            newNodes = null;
        }
    }
    
    public void displayData(){
        System.out.println("SAFE LIST:");
        forEach((T val)->{
            System.out.println("* " + val.toString());
        });
        System.out.println("END OF SAFE LIST");
    }
    
    
    public static void main(String[] args){
        SafeList<Integer> ll = new SafeList<>();
        ll.add(1);
        ll.add(2);
        ll.add(3);
        
        ll.forEach((Integer i)->{
            System.out.println(i);
            ll.add(i * i);
        });
        ll.displayData();
    }
}
