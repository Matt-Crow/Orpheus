package world.entities;

import java.awt.Graphics;
import util.Direction;
import world.events.Terminable;
import world.events.TerminateListener;
import world.WorldContent;
import java.io.Serializable;
import util.Coordinates;
import util.SafeList;

/**
 * The AbstractEntity class is used as the base for anything that moves and
 * exists within a World.
 */
public abstract class AbstractEntity implements Serializable, Terminable{
    private final WorldContent inWorld;
    
	private int x;
	private int y;
    private int maxSpeed;
    private double speedMultiplier;
    private int radius;
    private boolean isMoving;
    private Direction facing;
    
	private boolean shouldTerminate;
    private final SafeList<TerminateListener> terminateListeners;
    
	public final String id;
	private static int nextId = 0;
    
	public AbstractEntity(WorldContent world){
        x = 0;
        y = 0;
        maxSpeed = 0;
        speedMultiplier = 1.0;
        radius = 50;
        isMoving = false;
        facing = new Direction(0);
        
		id = "#" + nextId;
        inWorld = world;
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
    
	public final int getX(){
		return x;
	}
	public final int getY(){
		return y;
	}
	
    public final void setX(int xc){
        x = xc;
    }
    public final void setY(int yc){
        y = yc;
    }
    
    public final void setMaxSpeed(int speed){
		maxSpeed = speed;
	}
    public final int getMaxSpeed(){
        return maxSpeed;
    }
    
    public final void multiplySpeedBy(double f){
		speedMultiplier *= f;
	}
    
    // remove this later
    public final void clearSpeedFilter(){
        speedMultiplier = 1.0;
    }
    
    public final void setRadius(int r){
        if(r < 0){
            throw new IllegalArgumentException(String.format("radius must be non-negative, so %d isn't allowed", r));
        }
        radius = r;
    }
    public final int getRadius(){
        return radius;
    }
    
    public final void setIsMoving(boolean isMoving){
		this.isMoving = isMoving;
	}
	public final boolean getIsMoving(){
		return isMoving;
	}
    
    public final void setFacing(int degrees){
        facing.setDegrees(degrees);
    }
    public final Direction getFacing(){
		return facing;
	}
    
    public final void turnTo(int xCoord, int yCoord){
		facing = Direction.getDegreeByLengths(x, y, xCoord, yCoord);
	}
    
    public final boolean isWithin(int x, int y, int w, int h){
        return (
            x < this.x + radius //left
            && x + w > this.x - radius //right
            && y < this.y + radius //top
            && y + h > this.y - radius //bottom
        );
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
        return Coordinates.distanceBetween(this, e) <= e.getRadius() + radius;
	}
    
    /**
     *
     * @return how much this entity will move this frame
     */
	public final int getMomentum(){
		return (int)(maxSpeed * speedMultiplier);
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
     * This method is called at the start of a Battle.
     * Subclasses can override this method, but should ensure they call 
     * super.init() somewhere within their override
     */
    public void init(){
		// called by battle
        terminateListeners.clear();
        
		isMoving = false;
		speedMultiplier = 1.0;
		shouldTerminate = false;
	}
    
    /**
     * can be overridden, but subclasses should ensure they call super.update()
     * in their implementation.
     */
	public void update(){
		if(!shouldTerminate){
			updateMovement();
		}
	}
    
    /**
     * can be overridden, but subclasses should ensure they call 
     * super.updateMovement() in their implementation.
     */
    protected void updateMovement(){
		if(isMoving){
            x += getMomentum() * facing.getXMod();
            y += getMomentum() * facing.getYMod();
        }
		clearSpeedFilter();
	}
    
	public abstract void draw(Graphics g);
}