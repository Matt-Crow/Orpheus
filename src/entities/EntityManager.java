package entities;

import java.util.function.Consumer;

/**
 * Used to iterate over Entities,
 * supports insertions and deletions
 * even while iterating over the list
 * 
 * @author Matt
 */
public class EntityManager {
   private EntityNode head;
   private EntityNode tail;
   private boolean isIterating;
   
   public EntityManager(){
       head = null;
       tail = null;
       isIterating = false;
   }
   
   /**
    * Registers an Entity for updating, appending it to the tail.
    * In future versions, I may want to sort the items as I'm inserting
    * them, but while that would allow me to search easier, 
    * it would also slow this down
    * @param e 
    */
   public void add(Entity e){
       EntityNode en = new EntityNode(this, e);
       
       if(head == null){
           head = en;
           tail = en;
       } else {
           tail.setNext(en);
           tail = en;
       }
   }
   
   public void setHead(EntityNode en){
       if(en == null){
           throw new NullPointerException();
       }
       EntityNode curr = en;
       while(curr.hasParent()){
           curr = curr.getPrev();
       }
       head = curr;
   }
   
   public void headIsDead(){
       if(head.hasChild()){
           head = head.getNext();
       } else {
           //no more nodes
           head = null;
           tail = null;
       }
   }
   
   public void tailFailed(){
       if(tail.hasParent()){
           tail = tail.getPrev();
       } else {
           //no more nodes
           head = null;
           tail = null;
       }
   }
   
   public void forEach(Consumer<Entity> f){
       isIterating = true;
       EntityNode curr = head;
       while(curr != null){
           f.accept(curr.get());
           curr = curr.getNext();
       }
       isIterating = false;
   }
   
   public void print(){
       forEach((e)->{
           System.out.println(e.toString());
       });
   }
   
   public static void main(String[] args){
       EntityManager em = new EntityManager();
       Player p1 = new Player("A");
       Player p2 = new Player("B");
       Player p3 = new Player("C");
       Player p4 = new Player("D");
       Player p5 = new Player("E");
       em.add(p4);
       em.add(p3);
       em.add(p1);
       em.add(p5);
       em.add(p2);
       em.print();
   }
}
