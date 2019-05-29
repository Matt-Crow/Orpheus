package entities;

/**
 * Used by EntityManager to link Entities together
 * @author Matt Crow
 */
public class EntityNode {
    private final Entity entity;
    private EntityNode next;
    private boolean addedDuringIter;
    
    public EntityNode(Entity e, boolean managerIsIterating){
        if(e == null){
            throw new NullPointerException();
        }
        entity = e;
        addedDuringIter = managerIsIterating;
        next = null;
    }
    
    public Entity get(){
        return entity;
    }
    
    public void setNext(EntityNode e){
        if(e == null){
            throw new NullPointerException();
        }
        if(next != null){
            e.setNext(next);
        }
        next = e;
    }
    
    public EntityNode getNext(){
        return next;
    }
}
