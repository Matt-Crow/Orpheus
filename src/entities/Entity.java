package entities;

import initializers.Master;
import java.awt.Graphics;
import resources.Direction;
import battle.Team;
import actions.ActionRegister;
import ai.AI;

public abstract class Entity {
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
	private AI entityAI;
	
	// misc
	public final int id;
	private static int nextId = 0;
    
    //linked list stuff
    private Entity prev;
    private Entity next;
    
	public Entity(){
		id = nextId;
        prev = null;
        next = null;
		nextId++;
	}
    
    @Override
    public final boolean equals(Object o){
        return o instanceof Entity && ((Entity)o).id == id; 
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
	
	// movement functions
	public final int getX(){
		return x;
	}
	public final int getY(){
		return y;
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
		
		// keep entity on the battlefield
        int bound = Master.getCurrentBattle().getHost().getSize();
		if(x < 0){
			x = 0;
		} else if(x > bound){
			x = bound;
		}
		if(y < 0){
			y = 0;
		} else if(y > bound){
			y = bound;
		}
		
		speedFilter = 1.0;
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
    
    //add different versions in subclasses?
    public boolean checkForCollisions(Entity e){
		return distanceFrom(e) <= 100;
	}
    
	public final AI getEntityAI(){
		return entityAI;
	}
    
    //can't be final, as SeedProjectile needs to override
    //add on terminate?
	public void terminate(){
		shouldTerminate = true;
        
        if(prev != null){
            prev.next = next; //will set to null if this doesn't have a next
        }
        if(next != null){
            next.prev = prev;
        }
	}
	
	public final boolean getShouldTerminate(){
		return shouldTerminate;
	}
	
	// misc. methods
	public final int getId(){
		return id;
	}
    public void doInit(){
		// called by battle
        knockbackDir = new Direction(0);
        knockbackMag = 0;
        knockbackDur = 0;
        
		moving = false;
		speedFilter = 1.0;
		actReg = new ActionRegister(this);
		shouldTerminate = false;
		entityAI = new AI(this);
		
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
			entityAI.update();
			updateMovement();
			actReg.tripOnUpdate();
            update();
		}
	}
    
    public final void insertAfter(Entity e){
        if(e == this){
            System.out.println(this + " is trying to add itself as a child in Entity.insertAfter");
        } else {
            Entity temp = e.next;
            e.next = this;
            prev = e;
            next = temp;
            if(temp != null){
                temp.prev = this;
            }
        }
    }
    
    public final Entity getChild(){
        return next;
    }
    
    public abstract void init();
    public abstract void update();
	public abstract void draw(Graphics g);
}