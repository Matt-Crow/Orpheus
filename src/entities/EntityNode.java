package entities;

/**
 * Used by EntityManager to link Entities together
 * @author Matt Crow
 */
public class EntityNode{
    private final Entity entity;
    private EntityNode next;
    
    public EntityNode(Entity e){
        if(e == null){
            throw new NullPointerException();
        }
        entity = e;
        next = null;
    }
    
    @Override
    public EntityNode clone() throws CloneNotSupportedException{
        EntityNode ret = new EntityNode(entity);//(EntityNode)super.clone(); //not sure if I need this
        if(next != null){
            ret.next = next.clone();
        }
        return ret;
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
