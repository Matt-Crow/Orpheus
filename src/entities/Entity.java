package entities;

import initializers.Master;
import resources.Coordinates;
import resources.Direction;
import resources.Op;
import battle.Team;
import battle.Hitbox;
import actions.ActionRegister;
import ai.AI;

public class Entity {
	private int x;
	private int y;
	private Direction dir;
	private String willTurn;
	private int maxSpeed;
	private boolean moving;
	private boolean backwards;
	private double speedFilter;
	
	private Direction kbDir;
	private double kbDur;
	private int kbVelocity;
	
	private Team team;
	private boolean shouldTerminate;
	private ActionRegister actReg;
	private Hitbox hitbox;
	private AI entityAI;
	
	// linked list stuff
	// always have a head that does not get updated
	private Entity parent;
	private Entity child;
	private boolean hasParent;
	private boolean hasChild;
	
	public Entity(int xCoord, int yCoord, int degrees, int m){
		x = xCoord;
		y = yCoord;
		dir = new Direction(degrees);
		willTurn = "none";
		
		maxSpeed = m;
		moving = false;
		backwards = false;
		speedFilter = 1.0;
		
		kbDir = new Direction(0);
		kbDur = 0;
		kbVelocity = 0;
		
		actReg = new ActionRegister(this);
		shouldTerminate = false;
		hitbox = new Hitbox(this, 100, 100);
		entityAI = new AI(this);
		
		hasParent = false;
		hasChild = false;
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
	}
	public Coordinates getCoords(){
		return new Coordinates(getX(), getY(), this);
	}
	public void setDir(Direction d){
		dir = d;
	}
	public void setWillTurn(String s){
		willTurn = s;
	}
	public void setSpeed(int speed){
		maxSpeed = speed;
	}
	public int getSpeed(){
		return maxSpeed;
	}
	public void applySpeedFilter(double f){
		speedFilter *= f;
	}
	
	public Direction getDir(){
		return dir;
	}
	
	public void setMoving(boolean isMoving){
		moving = isMoving;
	}
	public void setBackwards(boolean back){
		backwards = back;
	}
	public boolean isBackwards(){
		return backwards;
	}
	
	public boolean getIsMoving(){
		return moving;
	}
	
	public int getMomentum(){
		int ret = (int)(maxSpeed * speedFilter);
		if(backwards){
			ret = (int)(-ret * 0.5);
		}
		return ret;
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
	
	public AI getEntityAI(){
		return entityAI;
	}
	
	public void terminate(){
		shouldTerminate = true;
		if(hasParent){
			parent.disableChild();
		}
		if(hasChild){
			child.disableParent();
		}
		if(hasParent && hasChild){
			parent.setChild(child);
			child.setParent(parent);
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
			
			if(e.getChild() == e){
				Op.add("ERROR");
				Op.dp();
			}
		}
	}
	public void removeChild(){
		if(hasChild){
			
		}
	}
	public boolean getHasParent(){
		return hasParent;
	}
	public boolean getHasChild(){
		return hasChild;
	}
	public void disableParent(){
		hasParent = false;
	}
	public void disableChild(){
		hasChild = false;
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
	
	public void turnToward(Direction d){
		int cDirNum = getDir().getDegrees();
		int dDirNum = d.getDegrees();
		boolean shouldLeft = true;
		
		if(cDirNum < dDirNum){
			shouldLeft = true;
		} else if(cDirNum > dDirNum){
			shouldLeft = false;
		} else {
			// already facing the correct way
			return;
		}
		
		double differenceBetween;
		if(cDirNum > dDirNum){
			differenceBetween = cDirNum - dDirNum;
		} else {
			differenceBetween = dDirNum - cDirNum;
		}
		
		if(differenceBetween > 180){
			shouldLeft = !shouldLeft;
		}
		
		if(shouldLeft){
			setWillTurn("left");
		} else {
			setWillTurn("right");
		}
	}
	
	public void move(){
		x += dir.getVector()[0] * getMomentum();
		y += dir.getVector()[1] * getMomentum();
	}
	
	public void updateMovement(){
		if((willTurn == "left") || (willTurn == "right")){
			turn(willTurn);
		}
		willTurn = "none";
		
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
			entityAI.update();
			updateMovement();
			actReg.tripOnUpdate();
		}
	}
}