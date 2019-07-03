package entities;

import java.io.Serializable;

/**
 * Used by EntityManager to link Entities together
 * @author Matt Crow
 */
public class EntityNode implements Serializable{
    private final EntityManager manager;
    private final Entity entity;
    private EntityNode prev;
    private EntityNode next;
    
    public EntityNode(EntityManager em, Entity e){
        if(e == null){
            throw new NullPointerException();
        }
        manager = em;
        entity = e;
        e.setNode(this);
        prev = null;
        next = null;
    }
    
    @Override
    public String toString(){
        return "Entity Node Containing Entity #" + entity.id;
    }
    
    public Entity get(){
        return entity;
    }
    
    public boolean hasParent(){
        return prev != null;
    }
    
    public boolean hasChild(){
        return next != null;
    }
    
    public void setPrev(EntityNode e){
        if(e == null){
            throw new NullPointerException();
        }
        if(e.hasParent() || e.hasChild()){
            System.out.println("Uh oh in EntityNode.setPrev");
        }
        if(prev != null){
            prev.next = e;
            e.prev = prev;
        }
        prev = e;
        e.next = this;
    }
    public void setNext(EntityNode e){
        if(e == null){
            throw new NullPointerException();
        }
        if(e.hasParent() || e.hasChild()){
            System.out.println("Uh oh in EntityNode.setNext");
        }
        if(next != null){
            next.prev = e;
            e.next = next;
        }
        next = e;
        e.prev = this;
    }
    
    /**
     * Inserts a node before this one,
     * that way, it isn't updated on the iteration
     * that it enters the game
     * @param e
     */
    public final void insert(Entity e){
        if(e == null){
            throw new NullPointerException();
        }
        if(e.equals(entity)){
            try {
                throw new Exception(this + " is trying to add itself as its parent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            EntityNode en = new EntityNode(manager, e);
            if(prev == null){
                manager.setHead(en);
            } else {
                prev.next = en;
                en.prev = this.prev;
            }
            prev = en;
            en.next = this;
        }
    }
    
    public final void delete(){
        if(prev == null){
            manager.headIsDead();
            if(next != null){
                next.prev = null; 
            }
        } else {
            prev.next = next;
        }
        
        if(next == null){
            manager.tailFailed();
            if(prev != null){
                prev.next = null;
            }
        } else {
            next.prev = prev;
        }
    }
    
    public EntityNode getPrev(){
        return prev;
    }
    
    public EntityNode getNext(){
        return next;
    }
}
