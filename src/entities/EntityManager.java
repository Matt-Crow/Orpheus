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
   private boolean isIterating;
   
   public EntityManager(){
       head = null;
       isIterating = false;
   }
   
   public void add(Entity e){
       EntityNode en = new EntityNode(e, isIterating);
       
       if(head == null){
           head = en;
       } else {
           EntityNode prev = null;
           EntityNode curr = head;
           while(curr != null && curr.get().id < e.id){
               prev = curr;
               curr = curr.getNext();
           }
           if(prev == null){
               //new head
               en.setNext(head);
               head = en;
           } else {
               prev.setNext(en);
           }
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
   
   public void update(){
       isIterating = true;
       EntityNode curr = head;
       while(curr != null){
           if(!curr.update()){
               System.out.println(curr.get().id + " did not update");
           }
           curr = curr.getNext();
       }
       isIterating = false;
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
