package entities;

import initializers.Master;
import resources.ActionRegister;
import resources.Direction;

public class Entity {
	private int x;
	private int y;
	private Direction dir;
	private String willTurn;
	private int momentum;
	private double speedFilter;
	private boolean moving;
	private boolean backwards;
	
	private Direction kbDir;
	private double kbDur;
	private int kbVelocity;
	
	private ActionRegister actReg;
	
	public Entity(int xCoord, int yCoord, int d, int m){
		x = xCoord;
		y = yCoord;
		dir = new Direction(d);
		willTurn = "none";
		
		momentum = m;
		speedFilter = 1.0;
		moving = false;
		backwards = false;
		
		kbDir = new Direction(0);
		kbDur = 0;
		kbVelocity = 0;
		
		actReg = new ActionRegister(this);
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
	public void setDir(Direction d){
		dir = d;
	}
	public void setWillTurn(String s){
		willTurn = s;
	}
	public void setMoving(boolean m){
		moving = m;
	}
	public void setBackwards(boolean b){
		backwards = b;
	}
	public void applySpeedFilter(double f){
		speedFilter *= f;
	}
	
	public Direction getDir(){
		return dir;
	}
	
	public int getMomentum(){
		return (int) (momentum * speedFilter);
	}
	
	public void applyKnockback(Direction d, int dur, int vel){
		kbDir = d;
		kbDur = dur;
		kbVelocity = vel;
	}
	
	public ActionRegister getActionRegister(){
		return actReg;
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
	public void moveBackwards(){
		x -= dir.getVector()[0] * getMomentum() * 0.5;
		y -= dir.getVector()[1] * getMomentum() * 0.5;
	}
	public void update(){
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
		} else if (backwards){
			moveBackwards();
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
		
		speedFilter = 1.0;
		
		actReg.tripOnUpdate();
	}
}