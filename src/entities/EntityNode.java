package entities;

/**
 * Used by EntityManager to link Entities together
 * @author Matt Crow
 */
public class EntityNode{
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
        prev = null;
        next = null;
    }
    
    @Override
    public EntityNode clone() throws CloneNotSupportedException{
        EntityNode ret = new EntityNode(manager, entity);//(EntityNode)super.clone(); //not sure if I need this
        if(prev != null){
            ret.prev = prev.clone();
        }
        if(next != null){
            ret.next = next.clone();
        }
        return ret;
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
        if(prev != null){
            e.setPrev(prev);
        }
        prev = e;
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
                next.prev = null; //don't call setter
                //as it will throw a null pointer exception
            }
        } else {
            next.setPrev(prev);
        }
        
        if(next == null){
            manager.tailFailed();
            if(prev != null){
                prev.next = null;
            }
        } else {
            prev.setNext(next);
        }
    }
    
    public EntityNode getPrev(){
        return prev;
    }
    
    public EntityNode getNext(){
        return next;
    }
}
