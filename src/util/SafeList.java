package util;

import java.util.function.Consumer;

/**
 * Provides a variation of a LinkedList that is immune to
 * concurrent modification exceptions.
 * 
 * The way it does this is by creating a second SafeList
 * which receives any changes that would be made to the original
 * while it is iterating. Once the SafeList is done iterating, it
 * copies the contents of the second list into itself.
 * <b>It is important to note that insertions and deletions performed
 * during iteration do not apply to the SafeList until after the forEach
 * method completes</b>
 * 
 * I could do this with less clutter by just
 * performing inserts behind the current node, or just in front of the head, but I'll handle that some
 * other time.
 * 
 * I may also use this class for some of the methods of Player and Entity
 * (managing statuses, action registers, teams, etc)
 * 
 * @author Matt Crow
 * @param <T> the type of element to include in this list
 */
public class SafeList<T>{
    private Node<T> head;
    private Node<T> tail;
    
    private boolean isIterating;
    //private SafeList<T> newNodes;
    
    public SafeList(){
        head = null;
        tail = null;
        
        isIterating = false;
        //newNodes = null;
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
    
    /**
     * Inserts a given value at the front of
     * the list. That way, if this is being
     * iterated over, but add is called, it 
     * will not alter the iteration: Yay no concmod.
     * @param val the value to add
     */
    public void add(T val){
        if(head == null){
            Node<T> nn = new Node<>(this, val);
            head = nn;
            tail = nn;
        } else {
            head.insert(val);
        }
        
        /*
        Node<T> nn = new Node<>(this, val);
        if(isIterating){
            if(newNodes == null){
                newNodes = new SafeList<>(this);
            }
            //System.out.println(this + " add " + val);
            newNodes.add(val);
        }else{
            //System.out.println(this + " add " + val);
            if(head == null){
                //no nodes
                head = nn;
                tail = nn;
            } else {
                tail.setNext(nn);
                //nn.setPrev(tail);
                tail = nn;
            }
        }*/
    }
    
    public void setHead(Node<T> nn){
        if(nn == null){
            throw new NullPointerException();
        }
        Node<T> curr = nn;
        while(curr.hasParent()){
            curr = curr.getPrev();
        }
        head = curr;
    }
    
    public boolean remove(T val){
        boolean wasRemoved = false;
        /*
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
        */
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
        /*
        if(newNodes != null){
            head = null;
            tail = null;
            newNodes.forEach((T val)->{
                add(val);
            });
            newNodes = null;
        }*/
    }
    
    public void displayData(){
        //SafeList<T> showList = (isIterating) ? newNodes : this;
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
            //ll.add(i * i);
            //ll.remove(i);
            ll.add(i + 1);
            ll.displayData();
        });
        ll.displayData();
        System.out.println(ll.isIterating);
    }
}
