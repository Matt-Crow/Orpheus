package entities;

import initializers.Master;
import java.awt.Graphics;
import resources.Direction;
import battle.Team;
import actions.ActionRegister;
import ai.AI;
import java.util.ArrayList;
import resources.Op;

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
	private final int id;
	private static int nextId = 0;
	
    
    
    //WIP
    private ArrayList<MovementVector> movement; //may want different data structure. Movement manager class?
    private int timeInVector;
    
    
    
    
    
	//constructors
	
	// depreciate later
	public Entity(int m){
		maxSpeed = m;
		
		id = nextId;
		nextId++;
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
		maxSpeed = speed;
	}
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
		return (int)(maxSpeed * speedFilter);
	}
	
	public final void turnTo(int xCoord, int yCoord){
		dir = Direction.getDegreeByLengths(x, y, xCoord, yCoord);
	}
	
	private void move(){
		x += dir.getVector()[0] * getMomentum();
		y += dir.getVector()[1] * getMomentum();
	}
	
    public final void addVector(int x, int y, int dur){
        movement.add(new MovementVector(x, y, dur));
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
		
        if(!movement.isEmpty()){
            Op.add(movement.get(0));
            Op.dp();
            x += movement.get(0).getX();
            y += movement.get(0).getY();
            timeInVector++;
            if(timeInVector >= movement.get(0).getDur()){
                timeInVector = 0;
                movement.remove(0);
            }
        }
		
		if(moving){
			move();
		}
		
		// keep entity on the battlefield
		if(x < 0){
			x = 0;
		} else if(x > Master.getCurrentBattle().getHost().getWidth()){
			x = Master.getCurrentBattle().getHost().getWidth();
		}
		if(y < 0){
			y = 0;
		} else if(y > Master.getCurrentBattle().getHost().getHeight()){
			y = Master.getCurrentBattle().getHost().getHeight();
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
    
    //add different versions in subclasses?
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
	public boolean checkForCollisions(Entity e){
		return x + 50 >= e.getX() - 50 
				&& x - 50 <= e.getX() + 50
				&& y + 50 >= e.getY() - 50 
				&& y - 50 <= e.getY() + 50;
	}
	public final AI getEntityAI(){
		return entityAI;
	}
    
    //can't be final, as SeedProjectile needs to override
	public void terminate(){
		shouldTerminate = true;
	}
	
	public final boolean getShouldTerminate(){
		return shouldTerminate;
	}
	
	// misc. methods
	public final int getId(){
		return id;
	}
    public void init(int xCoord, int yCoord, int degrees){
		// called by battle
		x = xCoord;
		y = yCoord;
		dir = new Direction(degrees);
		
		moving = false;
		speedFilter = 1.0;
		actReg = new ActionRegister(this);
		shouldTerminate = false;
		entityAI = new AI(this);
		
		hasFocus = false;
        
        
        
        movement = new ArrayList<>();
        timeInVector = 0;
	}
	public final void doUpdate(){
		if(!shouldTerminate){
			entityAI.update();
			updateMovement();
			actReg.tripOnUpdate();
            update();
		}
	}
    
    public abstract void update();
	public abstract void draw(Graphics g);
}