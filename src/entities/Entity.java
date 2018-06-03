package entities;

import initializers.Master;
import java.awt.Graphics;
import resources.Direction;
import battle.Team;
import actions.ActionRegister;
import ai.AI;

public class Entity {
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
	 * Knockback related stuff, working much like regular movement:
	 * dir -> kbDir
	 * maxSpeed -> kbVelocity 
	 * 
	 * however, kbDur is the duration (in frames) that the knockback lasts
	 * Depreciate later if too annoying
	 */
	private Direction kbDir;
	private double kbDur;
	private int kbVelocity;
	
	/*
	 * (focusX, focusY) is a point that the entity is trying to reach
	 */
	private int focusX;
	private int focusY;
	private boolean hasFocus;
	
	/*
	 * In-battle stuff
	 * 
	 * shouldTeminate is set to true after the entity frees itself from its node chain
	 * more on that in linked list section
	 * 
	 * actReg is used to store actions (onBeHit, onUpdate, etc.) see the actions package for more details
	 * entityAI is the AI that runs this. All entities have this AI, but it must be manually enabled
	 */
	private Team team;
	private boolean shouldTerminate;
	private ActionRegister actReg;
	private AI entityAI;
	
	/*
	 * Linked list stuff 
	 * 
	 * depreciating
	 */
	private Entity parent;
	private Entity child;
	private boolean hasParent;
	private boolean hasChild;
	
	// misc
	private boolean skipUpdate; // use when traversing chunks
	private EntityType type; // used for when downcasted
	private int id;
	private static int nextId = 0;
	
	//constructors
	
	// depreciate later
	public Entity(int m){
		maxSpeed = m;
		
		hasParent = false;
		hasChild = false;
		
		type = EntityType.RAW;
		
		id = nextId;
		nextId++;
	}
	
	//node chain head
	public Entity(){
		hasChild = false;
	}
	
	
	
	// movement functions
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Direction getDir(){
		return dir;
	}
	public void setSpeed(int speed){
		maxSpeed = speed;
	}
	public void applySpeedFilter(double f){
		speedFilter *= f;
	}
	public void setMoving(boolean isMoving){
		moving = isMoving;
	}
	public boolean getIsMoving(){
		return moving;
	}
	public int getMomentum(){
		return (int)(maxSpeed * speedFilter);
	}
	
	// depreciate later
	public void turn(String d){
		int amount = 360 / Master.TICKSTOROTATE;
		if(d == "left"){
			dir.turnClockwise(amount);
		} else {
			dir.turnCounterClockwise(amount);
		}
	}
	public void turnTo(int xCoord, int yCoord){
		dir = Direction.getDegreeByLengths(x, y, xCoord, yCoord);
	}
	
	public void move(){
		x += dir.getVector()[0] * getMomentum();
		y += dir.getVector()[1] * getMomentum();
	}
	public void applyKnockback(Direction d, int dur, int vel){
		kbDir = d;
		kbDur = dur;
		kbVelocity = vel;
	}
	public void updateMovement(){
		if(hasFocus){
			if(withinFocus()){
				hasFocus = false;
				setMoving(false);
			}else{
				turnToFocus();
				setMoving(true);
			}
		}
		
		if(kbDur > 0){
			x += kbDir.getVector()[0] * kbVelocity;
			y += kbDir.getVector()[1] * kbVelocity;
			kbDur -= 1;
		} else {
			applyKnockback(new Direction(0), 0, 0);
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
	public void setFocus(int xCoord, int yCoord){
		focusX = xCoord;
		focusY = yCoord;
		hasFocus = true;
	}
	public void setFocus(Entity e){
		setFocus(e.getX(), e.getY());
	}
	public void turnToFocus(){
		turnTo(focusX, focusY);
	}
	public boolean withinFocus(){
		// returns if has reached focal point
		boolean withinX = Math.abs(getX() - focusX) < maxSpeed;
		boolean withinY = Math.abs(getY() - focusY) < maxSpeed;
		return withinX && withinY;
	}
	
	
	
	// inbattle methods
	public void init(int xCoord, int yCoord, int degrees){
		// called by battle
		x = xCoord;
		y = yCoord;
		dir = new Direction(degrees);
		
		moving = false;
		speedFilter = 1.0;
		kbDir = new Direction(0);
		kbDur = 0;
		kbVelocity = 0;
		actReg = new ActionRegister(this);
		shouldTerminate = false;
		entityAI = new AI(this);
		skipUpdate = false;
		
		hasFocus = false;
	}
	public void setTeam(Team t){
		team = t;
	}
	public Team getTeam(){
		return team;
	}
	public ActionRegister getActionRegister(){
		return actReg;
	}
	public boolean checkForCollisions(Entity e){
		return x + 50 >= e.getX() - 50 
				&& x - 50 <= e.getX() + 50
				&& y + 50 >= e.getY() - 50 
				&& y - 50 <= e.getY() + 50;
	}
	public AI getEntityAI(){
		return entityAI;
	}
	
	
	
	
	// linked list methods
	public void terminate(){
		shouldTerminate = true;
		closeNodeGap();
	}
	public void closeNodeGap(){
		if(hasParent && hasChild){
			parent.setChild(child);
			child.setParent(parent);
			setHasParent(false);
			setHasChild(false);
		}
		else if(hasParent){
			parent.setHasChild(false);
			setHasParent(false);
		}
		else if(hasChild){
			child.setHasParent(false);
			setHasChild(false);
		}
		
	}
	public boolean getShouldTerminate(){
		return shouldTerminate;
	}
	public void setParent(Entity e){
		parent = e;
		hasParent = true;
		if(!e.getHasChild() || e.getChild() != this){
			e.setChild(this);
		}
	}
	public void setChild(Entity e){
		child = e;
		hasChild = true;
		if(!e.getHasParent() || e.getParent() != this){
			e.setParent(this);
		}
	}
	public void insertChild(Entity e){
		if(!hasChild){
			setChild(e);
		} else {
			Entity old = child;
			setChild(e);
			e.setChild(old);
		}
		e.setParent(this);
	}
	public boolean getHasParent(){
		return hasParent;
	}
	public boolean getHasChild(){
		return hasChild;
	}
	public void setHasParent(boolean b){
		hasParent = b;
	}
	public void setHasChild(boolean b){
		hasChild = b;
	}
	public Entity getParent(){
		return parent;
	}
	public Entity getChild(){
		return child;
	}
	
	
	
	
	// misc. methods
	public void setType(EntityType e){
		type = e;
	}
	public EntityType getType(){
		return type;
	}
	public int getId(){
		return id;
	}
	public void update(){
		if(!shouldTerminate){
			if(!skipUpdate){
				entityAI.update();
				updateMovement();
				actReg.tripOnUpdate();
			} else {
				skipUpdate = false;
			}
		}
	}
	public void draw(Graphics g){
		// leave blank
	}
}