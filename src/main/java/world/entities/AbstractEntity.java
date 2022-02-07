package world.entities;

import java.awt.Graphics;
import world.events.Terminable;
import world.events.TerminateListener;
import world.WorldContent;
import java.io.Serializable;
import util.Coordinates;
import util.SafeList;
import world.battle.Team;
import world.events.ActionRegister;

/**
 * The AbstractEntity class is used as the base for anything that moves and
 * exists within a World.
 */
public abstract class AbstractEntity extends AbstractPrimitiveEntity implements Serializable, Terminable{
    private final WorldContent inWorld;
    
    private double speedMultiplier;
    
    private final ActionRegister actReg;
    private Team team;
        
	private boolean shouldTerminate;
    private final SafeList<TerminateListener> terminateListeners;
    
	public final String id;
	private static int nextId = 0;
    
	public AbstractEntity(WorldContent world){
        speedMultiplier = 1.0;        
		id = "#" + nextId;
        inWorld = world;
        actReg = new ActionRegister(this);
        terminateListeners = new SafeList<>();
		nextId++;
	}
    
    @Override
    public final boolean equals(Object o){
        return o != null && o instanceof AbstractEntity && o == this && ((AbstractEntity)o).id.equals(id); 
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString(){
        return "Entity #" + id;
    }
    
    public final WorldContent getWorld(){
        return inWorld;
    }
    
    public final void multiplySpeedBy(double f){
		speedMultiplier *= f;
	}
    
    // remove this later
    public final void clearSpeedFilter(){
        speedMultiplier = 1.0;
    }
    
    /**
     * Checks if this entity collides with another entity.
     * Subclasses should overload this with each subclass of AbstractEntity
     * that needs special reactions
     * 
     * @param e the AbstractEntity to check for collisions with
     * @return whether or not this collides with the given AbstractEntity
     */
    public final boolean isCollidingWith(AbstractEntity e){
        return Coordinates.distanceBetween(this, e) <= e.getRadius() + getRadius();
	}
    
    /**
     *
     * @return how much this entity will move this frame
     */
    @Override
	public final int getMomentum(){
		return (int)(getMaxSpeed() * speedMultiplier);
	}
    
    public final ActionRegister getActionRegister(){
		return actReg;
	}
    
    public final void setTeam(Team t){
		team = t;
	}
	public final Team getTeam(){
		return team;
	}
    
    @Override
    public void addTerminationListener(TerminateListener listen) {
        terminateListeners.add(listen);
    }

    @Override
    public boolean removeTerminationListener(TerminateListener listen) {
        return terminateListeners.remove(listen);
    }
    
    @Override
	public void terminate(){
		shouldTerminate = true;
        terminateListeners.forEach((TerminateListener tl)->{
            tl.objectWasTerminated(this);
        });
	}
	
	public final boolean getShouldTerminate(){
		return shouldTerminate;
	}
    
    /**
     * Inserts an AbstractEntity into this' EntityNode chain.
     * Since the AbstractEntity is inserted before this one, it will not be 
     * updated during this iteration of EntityManager.update
     * @param e the AbstractEntity to insert before this one
     */
    public final void spawn(AbstractEntity e){
        if(e == null){
            throw new NullPointerException();
        }
        team.add(e);
    }
    
    
    /**
     * This method is called at the start of a Battle.
     * Subclasses can override this method, but should ensure they call 
     * super.init() somewhere within their override
     */
    @Override
    public void init(){
		// called by battle
        terminateListeners.clear();
        actReg.reset();
		setIsMoving(false);
		speedMultiplier = 1.0;
		shouldTerminate = false;
	}
    
    /**
     * can be overridden, but subclasses should ensure they call super.update()
     * in their implementation.
     */
    @Override
	public void update(){
		if(!shouldTerminate){
			super.update();
            actReg.triggerOnUpdate();
		}
	}
    
    @Override
    protected void updateMovement(){
		super.updateMovement();
		clearSpeedFilter();
	}
    
    @Override
	public abstract void draw(Graphics g);
}