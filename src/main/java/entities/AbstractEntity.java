package entities;

import java.awt.Graphics;
import util.Direction;
import battle.Team;
import actions.Terminable;
import actions.TerminateListener;
import controllers.Master;
import world.World;
import java.io.Serializable;
import util.SafeList;

/**
 * The AbstractEntity class is used as the base for anything that has to interact with players in game.
 * this class only includes behavior associated with vector-based movement and termination.
 */
public abstract class AbstractEntity implements Serializable, Terminable{
	/*
	 * Position fields
	 */
	private int x;
	private int y;
    private int radius; //used for collisions
	private Direction dir; // the direction the entity is facing, ranging from 0-359 degrees, with 0 being the positive x axis, turning counterclockwise
	private int maxSpeed;
	private boolean moving;
	private double speedFilter; // amount the entity's speed is multiplied by when moving. May depreciate later
	
	/*
	 * In-battle stuff
	 */
	private Team team;
	private boolean shouldTerminate;
	
    private final SafeList<TerminateListener> terminateListeners; //you just can't wait for me to die, can you!
    
    private World inWorld; //the world this is currently in
    
	public final String id;
	private static int nextId = 0;
    
	public AbstractEntity(){
		id = Master.SERVER.getIpAddr() + "#" + nextId;
        inWorld = null;
        radius = 50;
        terminateListeners = new SafeList<>();
        dir = new Direction(0);
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
    
    public void setWorld(World w){
        if(w != null){
            inWorld = w;
        } else {
            throw new NullPointerException();
        }
    }
    public World getWorld(){
        return inWorld;
    }
    
    
	// movement functions
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
    public final void setFacing(int degrees){
        dir.setDegrees(degrees);
    }
    
    
    public final Direction getDir(){
		return dir;
	}
    
    /**
     * Sets the movement speed of this entity
     * @param speed
     */
	public final void setSpeed(int speed){
		maxSpeed = speed;
	}
    
    public final int getSpeed(){
        return maxSpeed;
    }
    
    //change later
     /**
     * @param f : the amount this entity's speed will be multiplied by
     */
	public final void applySpeedFilter(double f){
		speedFilter *= f;
	}
    public final void clearSpeedFilter(){
        speedFilter = 1.0;
    }
	public final void setMoving(boolean isMoving){
		moving = isMoving;
	}
	public final boolean getIsMoving(){
		return moving;
	}
	public final int getMomentum(){
        /**
         * returns how much this entity will move
         */
		return (int)(maxSpeed * speedFilter);
	}
	
	public final void turnTo(int xCoord, int yCoord){
        /**
         * Rotates the entity to face the given point
         */
		dir = Direction.getDegreeByLengths(x, y, xCoord, yCoord);
	}
    
	public void updateMovement(){
		if(moving){
            x += getMomentum() * dir.getXMod();
            y += getMomentum() * dir.getYMod();
        }
		clearSpeedFilter();
	}
    
    public final void setRadius(int r){
        radius = (r >= 0) ? r : -r;
    }
    public final int getRadius(){
        return radius;
    }
	
	// inbattle methods
	public final void setTeam(Team t){
		team = t;
	}
	public final Team getTeam(){
		return team;
	}
	
	
    public double distanceFrom(int xc, int yc){
        return Math.sqrt(Math.pow(xc - x, 2) + Math.pow(yc - y, 2));
    }
    public double distanceFrom(AbstractEntity e){
        return distanceFrom(e.getX(), e.getY());
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
 that needs special reactions
     * 
     * @param e the AbstractEntity to check for collisions with
     * @return whether or not this collides with the given AbstractEntity
     */
    public boolean checkForCollisions(AbstractEntity e){
        return distanceFrom(e) <= e.getRadius() + radius;
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
    
    public final void doInit(){
		// called by battle
        terminateListeners.clear();
        
		moving = false;
		speedFilter = 1.0;
		shouldTerminate = false;
        init();
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
    
    public abstract void init();
    public abstract void update();
	public abstract void draw(Graphics g);

    @Override
    public void addTerminationListener(TerminateListener listen) {
        terminateListeners.add(listen);
    }

    @Override
    public boolean removeTerminationListener(TerminateListener listen) {
        return terminateListeners.remove(listen);
    }
}