package world.entities;

import java.awt.Graphics;
import util.Direction;
import world.battle.Team;
import world.events.Terminable;
import world.events.TerminateListener;
import world.WorldContent;
import java.io.Serializable;
import util.SafeList;

/**
 * The AbstractEntity class is used as the base for anything that has to interact with players in game.
 * this class only includes behavior associated with vector-based movement and termination.
 */
public abstract class AbstractEntity implements Serializable, Terminable{
	private int x;
	private int y;
    private int maxSpeed;
    private double speedMultiplier;
    private int radius;
    private boolean isMoving;
    private Direction facing;
    
    
	
	/*
	 * In-battle stuff
	 */
	private Team team;
	private boolean shouldTerminate;
	
    private final SafeList<TerminateListener> terminateListeners; //you just can't wait for me to die, can you!
    
    private WorldContent inWorld; //the world this is currently in
    
	public final String id;
	private static int nextId = 0;
    
	public AbstractEntity(){
        x = 0;
        y = 0;
        maxSpeed = 0;
        speedMultiplier = 1.0;
        radius = 50;
        isMoving = false;
        facing = new Direction(0);
        
		id = "#" + nextId;
        inWorld = null;
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
    
    public double distanceFrom(AbstractEntity e){
        return distanceFrom(e.getX(), e.getY());
    }
    public double distanceFrom(int xc, int yc){
        return Math.sqrt(Math.pow(xc - x, 2) + Math.pow(yc - y, 2));
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
        return distanceFrom(e) <= e.getRadius() + radius;
	}
    
    public void updateMovement(){
		if(isMoving){
            x += getMomentum() * facing.getXMod();
            y += getMomentum() * facing.getYMod();
        }
		clearSpeedFilter();
	}
    
    /**
     *
     * @return how much this entity will move this frame
     */
	public final int getMomentum(){
		return (int)(maxSpeed * speedMultiplier);
	}
    
    
    
    
    
    
    
    
    public void setWorld(WorldContent w){
        if(w == null){
            throw new NullPointerException();
        } else {
            inWorld = w;
        }
    }
    public WorldContent getWorld(){
        return inWorld;
    }
	
	// inbattle methods
	public final void setTeam(Team t){
		team = t;
	}
	public final Team getTeam(){
		return team;
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
    
    public final void init(){
		// called by battle
        terminateListeners.clear();
        
		isMoving = false;
		speedMultiplier = 1.0;
		shouldTerminate = false;
        doInit();
	}
    
	public final void doUpdate(){
		if(!shouldTerminate){
			updateMovement();
            update();
		}
	}
    
    /**
     * Inserts an AbstractEntity into this' EntityNode chain.
     * Since the AbstractEntity is inserted before this one,
 it will not be updated during this iteration of
 EntityManager.update
     * @param e the AbstractEntity to insert before this one
     */
    public final void spawn(AbstractEntity e){
        if(e == null){
            throw new NullPointerException();
        }
        e.setWorld(inWorld);
        team.add(e);
    }
    
    @Override
    public void addTerminationListener(TerminateListener listen) {
        terminateListeners.add(listen);
    }

    @Override
    public boolean removeTerminationListener(TerminateListener listen) {
        return terminateListeners.remove(listen);
    }
    
    public abstract void doInit();
    public abstract void update();
	public abstract void draw(Graphics g);
}