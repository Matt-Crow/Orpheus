package entities;

import java.awt.Graphics;
import util.Direction;
import battle.Team;
import actions.ActionRegister;
import controllers.World;
import java.io.Serializable;
import static java.lang.System.out;
import util.Node;

public abstract class Entity implements Serializable{
	/**
	 * The Entity class is used as the base for anything that has to interact with players in game
	 */
	
	/*
	 * Position fields
	 * 
	 * move to a movement manager class later?
	 */
	private int x;
	private int y;
    private int radius; //used for collisions
	private Direction dir; // the direction the entity is facing, ranging from 0-359 degrees, with 0 being the positive x axis, turning counterclockwise
	private int maxSpeed;
	private boolean moving;
	private double speedFilter; // amount the entity's speed is multiplied by when moving. May depreciate later
	
    private Direction knockbackDir;
    private int knockbackMag;
    private int knockbackDur;
    
	/*
	 * (focusX, focusY) is a point that the entity is trying to reach
	 */
	private int focusX;
	private int focusY;
	private boolean hasFocus;
	
	/*
	 * In-battle stuff
	 * 
	 * shouldTeminate is set to true once the entity should be deleted
	 * 
	 * actReg is used to store actions (onBeHit, onUpdate, etc.) see the actions package for more details
	 * entityAI is the AI that runs this. All entities have this AI, but it must be manually enabled
	 */
	private Team team;
	private boolean shouldTerminate;
	private ActionRegister actReg;
	
	// misc
	public final int id;
	private static int nextId = 0;
    
    
    private Node<Entity> inNode; //the EntityNode containing this
    
    private World inWorld; //the world this is currently in
    
	public Entity(){
		id = nextId;
        inNode = null;
        inWorld = null;
        radius = 50;
		nextId++;
	}
    
    public void detailedDisplayData(){
        out.println("ENTITY #" + id);
        out.println("X: " + x);
        out.println("Y: " + y);
        out.println("WORLD: " + inWorld);
    }
    
    @Override
    public final boolean equals(Object o){
        return o != null && o instanceof Entity && o == this && ((Entity)o).id == id; 
    }

    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.id;
        return hash;
    }
    
    @Override
    public String toString(){
        return "Entity #" + id;
    }
	
    
    /**
     * Notifies this that an Node contains this.
     * @param n the Node containing this
     */
    public void setNode(Node<Entity> n){
        inNode = n;
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
    
    
    public final Direction getDir(){
		return dir;
	}
	public final void setSpeed(int speed){
        /**
         * Sets the movement speed of this entity
         */
		maxSpeed = speed;
	}
    
    //change later
     /**
     * @param f : the amount this entity's speed will be multiplied by
     */
	public final void applySpeedFilter(double f){
		speedFilter *= f;
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
	
    public final void knockBack(int mag, Direction d, int dur){
        /**
         * @param mag : the total distance this entity will be knocked back
         * @param d : the direction this entity is knocked back
         * @param dur : the number of frames this will be knocked back for
         */
        knockbackMag = mag / dur;
        knockbackDir = d;
        knockbackDur = dur;
    }
    
	private void updateMovement(){
		if(hasFocus){
			if(withinFocus()){
				hasFocus = false;
				setMoving(false);
			}else{
				turnToFocus();
				setMoving(true);
			}
		}
		
        if(knockbackDur <= 0){
            //can move if not knocked back
            if(moving){
                x += getMomentum() * dir.getXMod();
                y += getMomentum() * dir.getYMod();
            }
        } else {
            x += knockbackMag * knockbackDir.getXMod();
            y += knockbackMag * knockbackDir.getYMod();
            knockbackDur--;
        }
		
		speedFilter = 1.0;
	}
    
    public final void setRadius(int r){
        radius = (r >= 0) ? r : -r;
    }
    public final int getRadius(){
        return radius;
    }
    
    
	//focus related methods
	public final void setFocus(int xCoord, int yCoord){
		focusX = xCoord;
		focusY = yCoord;
		hasFocus = true;
	}
	public final void setFocus(Entity e){
		setFocus(e.getX(), e.getY());
	}
	public final void turnToFocus(){
		turnTo(focusX, focusY);
	}
	public boolean withinFocus(){
		// returns if has reached focal point
		boolean withinX = Math.abs(getX() - focusX) < maxSpeed;
		boolean withinY = Math.abs(getY() - focusY) < maxSpeed;
		return withinX && withinY;
	}
	
	// inbattle methods
	public final void setTeam(Team t){
		team = t;
	}
	public final Team getTeam(){
		return team;
	}
	public final ActionRegister getActionRegister(){
		return actReg;
	}
	
    
    public double distanceFrom(int xc, int yc){
        return Math.sqrt(Math.pow(xc - x, 2) + Math.pow(yc - y, 2));
    }
    public double distanceFrom(Entity e){
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
     * Subclasses should overload this with each subclass of Entity
     * that needs special reactions
     * 
     * @param e the Entity to check for collisions with
     * @return whether or not this collides with the given Entity
     */
    public boolean checkForCollisions(Entity e){
        return distanceFrom(e) <= e.getRadius() + radius;
	}
    
    //can't be final, as SeedProjectile needs to override
    //add on terminate?
	public void terminate(){
		shouldTerminate = true;
        if(inNode != null){
            inNode.delete();
        }
	}
	
	public final boolean getShouldTerminate(){
		return shouldTerminate;
	}
    
    public final void doInit(){
		// called by battle
        knockbackDir = new Direction(0);
        knockbackMag = 0;
        knockbackDur = 0;
        
		moving = false;
		speedFilter = 1.0;
		actReg = new ActionRegister(this);
		shouldTerminate = false;
		
		hasFocus = false;
        init();
	}
    
    public final void initPos(int xCoord, int yCoord, int degrees){
        x = xCoord;
		y = yCoord;
		dir = new Direction(degrees);
    }
    
	public final void doUpdate(){
		if(!shouldTerminate){
			updateMovement();
			actReg.triggerOnUpdate();
            update();
		}
	}
    
    /**
     * Inserts an Entity into this' EntityNode chain.
     * Since the Entity is inserted before this one,
     * it will not be updated during this iteration of
     * EntityManager.update
     * @param e the Entity to insert before this one
     */
    public final void spawn(Entity e){
        if(e == null){
            throw new NullPointerException();
        }
        e.setWorld(inWorld);
        if(inNode != null){
            //System.out.println("before spawn: " + team.length());
            inNode.insert(e);
            //since insert puts the new node in front of this one...
            e.setNode(inNode.getPrev());
            //System.out.println("after: " + team.length());
            if(e != inNode.getPrev().getValue()){
                System.err.println("uh oh in Entity.spawn");
            }
        } else {
            System.out.println("Cannot spawn: Not in a node!!! (Entity.spawn)");
        }
    }
    
    public abstract void init();
    public abstract void update();
	public abstract void draw(Graphics g);
}