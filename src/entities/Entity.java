package entities;

import initializers.Master;
import java.awt.Graphics;
import resources.Direction;
import battle.Chunk;
import battle.Team;
import battle.Hitbox;
import actions.ActionRegister;
import ai.AI;

public class Entity {
	private int x;
	private int y;
	private Direction dir;
	private int maxSpeed;
	private boolean moving;
	private double speedFilter;
	
	private Direction kbDir;
	private double kbDur;
	private int kbVelocity;
	
	private Team team;
	private boolean shouldTerminate;
	private ActionRegister actReg;
	private Hitbox hitbox;
	private AI entityAI;
	
	private Chunk chunk;
	private int id;
	private static int nextId = 0;
	
	// linked list stuff
	// always have a head that does not get updated
	private Entity parent;
	private Entity child;
	private boolean hasParent;
	private boolean hasChild;
	
	private boolean skipUpdate; // use when traversing chunks
	private EntityType type; // used for when downcasted
	
	
	// focus is the point we want to move to
	private int focusX;
	private int focusY;
	private boolean hasFocus;
	// not fully implemented
	
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
	
	public void setType(EntityType e){
		type = e;
	}
	public EntityType getType(){
		return type;
	}
	
	public void init(int xCoord, int yCoord, int degrees){
		initPos(xCoord, yCoord, degrees);
		moving = false;
		speedFilter = 1.0;
		kbDir = new Direction(0);
		kbDur = 0;
		kbVelocity = 0;
		actReg = new ActionRegister(this);
		shouldTerminate = false;
		hitbox = new Hitbox(this, 100, 100);
		entityAI = new AI(this);
		skipUpdate = false;
		
		hasFocus = false;
	}
	
	public void initPos(int xCoord, int yCoord, int degrees){
		setCoords(xCoord, yCoord);
		dir = new Direction(degrees);
	}
	
	public int getId(){
		return id;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	
	public void setCoords(int xCoord, int yCoord){
		x = xCoord;
		y = yCoord;
		chunk = Master.getCurrentBattle().getHost().getChunkContaining(xCoord, yCoord);
		chunk.register(this);
	}
	
	public void setDir(Direction d){
		dir = d;
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
	
	public void setFocus(int xCoord, int yCoord){
		focusX = xCoord;
		focusY = yCoord;
		hasFocus = true;
	}
	public void setFocus(Entity e){
		setFocus(e.getX(), e.getY());
	}
	
	public Chunk getChunk(){
		return chunk;
	}
	
	public void applyKnockback(Direction d, int dur, int vel){
		kbDir = d;
		kbDur = dur;
		kbVelocity = vel;
	}
	
	public ActionRegister getActionRegister(){
		return actReg;
	}
	
	public void setTeam(Team t){
		team = t;
	}
	
	public Team getTeam(){
		return team;
	}
	
	public Hitbox getHitbox(){
		return hitbox;
	}
	
	public boolean checkForCollisions(Entity e){
		return hitbox.checkForIntercept(e.getHitbox());
	}
	
	public AI getEntityAI(){
		return entityAI;
	}
	
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
	
	public void turn(String d){
		int amount = 360 / Master.TICKSTOROTATE;
		if(d == "left"){
			dir.turnClockwise(amount);
		} else {
			dir.turnCounterClockwise(amount);
		}
	}
	
	public void move(){
		x += dir.getVector()[0] * getMomentum();
		y += dir.getVector()[1] * getMomentum();
	}
	
	public void turnTo(int xCoord, int yCoord){
		setDir(Direction.getDegreeByLengths(x, y, xCoord, yCoord));
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
		
		hitbox.updatePosition();
		if(chunk == null){
			chunk = Master.getCurrentBattle().getHost().getChunkContaining(x, y);
		}
		if(!chunk.contains(x, y)){
			closeNodeGap();
			
			if(chunk.getX() + chunk.getSize() <= x && chunk.getY() + chunk.getSize() <= y){
				// if the chunk is to our upper left, the new chunk has not yet updated
				skipUpdate = true; // so don't update us twice!
			}
			chunk = Master.getCurrentBattle().getHost().getChunkContaining(x, y);
			chunk.register(this);
		}
		
		speedFilter = 1.0;
	}
	
	public void updateAllChildren(){
		Entity current = this;
		while(current.getHasChild()){
			current = current.getChild();
			current.update();
		}
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
		
	}
}