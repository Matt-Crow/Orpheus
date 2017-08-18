package entities;

import initializers.Master;
import java.util.ArrayList;
import resources.OnHitAction;
import resources.OnUpdateAction;
import resources.Direction;

public class Entity {
	private int x;
	private int y;
	private double dirNum;
	private int momentum;
	private double speedFilter;
	private boolean moving;
	private boolean backwards;
	
	private double kbDirNum;
	private double kbDur;
	private int kbVelocity;
	
	private ArrayList<OnHitAction> onHitRegister;
	private ArrayList<OnHitAction> onBeHitRegister;
	private ArrayList<OnHitAction> onMeleeHitRegister;
	private ArrayList<OnHitAction> onBeMeleeHitRegister;
	private ArrayList<OnUpdateAction> onUpdateRegister;
	
	public Entity(int xCoord, int yCoord, double directionNumber, int m){
		x = xCoord;
		y = yCoord;
		dirNum = directionNumber;
		momentum = m;
		speedFilter = 1.0;
		moving = false;
		backwards = false;
		
		kbDirNum = 0;
		kbDur = 0;
		kbVelocity = 0;
		
		onHitRegister = new ArrayList<>();
		onBeHitRegister = new ArrayList<>();
		onMeleeHitRegister = new ArrayList<>();
		onBeMeleeHitRegister = new ArrayList<>();
		onUpdateRegister = new ArrayList<>();
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
	public void setDirNum(double dir){
		dirNum = dir;
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
	
	public double[] getVector(){
		return new Direction(dirNum).getVector();
	}
	public double getDirNum(){
		return dirNum;
	}
	
	public int getMomentum(){
		return (int) (momentum * speedFilter);
	}
	
	public void applyKnockback(double dirN, int dur, int vel){
		kbDirNum = dirN;
		kbDur = dur;
		kbVelocity = vel;
	}
	public double[] getKBVector(){
		return new Direction(kbDirNum).getVector();
	}
	
	public void addOnHit(OnHitAction a){
		onHitRegister.add(a);
	}
	public void addOnBeHit(OnHitAction a){
		onBeHitRegister.add(a);
	}
	public void addOnMeleeHit(OnHitAction a){
		onMeleeHitRegister.add(a);
	}
	public void addOnBeMeleeHit(OnHitAction a){
		onBeMeleeHitRegister.add(a);
	}
	public void addOnUpdate(OnUpdateAction a){
		onUpdateRegister.add(a);
	}
	
	public void resetTrips(){
		onHitRegister = new ArrayList<>();
		onBeHitRegister = new ArrayList<>();
		onMeleeHitRegister = new ArrayList<>();
		onBeMeleeHitRegister = new ArrayList<>();
		onUpdateRegister = new ArrayList<>();
		
		speedFilter = 1.0;
	}
	public void tripOnHit(Player hit){
		for(OnHitAction a : onHitRegister){
			a.setHitter(this);
			a.setHit(hit);
			a.trip();
		}
	}
	public void tripOnBeHit(Player hitBy){
		for(OnHitAction a : onBeHitRegister){
			a.setHitter(hitBy);
			a.setHit((Player) this);
			a.trip();
		}
	}
	public void tripOnMeleeHit(Player hit){
		for(OnHitAction a : onMeleeHitRegister){
			a.setHitter(this);
			a.setHit(hit);
			a.trip();
		}
		tripOnHit(hit);
	}
	public void tripOnBeMeleeHit(Player hitBy){
		for(OnHitAction a : onBeMeleeHitRegister){
			a.setHitter(this);
			a.setHit((Player) this);
			a.trip();
		}
		tripOnBeHit(hitBy);
	}
	public void tripOnUpdate(){
		for(OnUpdateAction a : onUpdateRegister){
			a.trip();
		}
	}
	
	public void turn(String dir){
		if(dir == "left"){
			dirNum += 0.1;
		} else {
			dirNum -= 0.1;
		}
		if(dirNum < 0){
			dirNum = 1.9;
		} else if(dirNum > 1.9){
			dirNum = 0;
		}
	}
	
	public void move(){
		x += getVector()[0] * getMomentum();
		y += getVector()[1] * getMomentum();
		
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
	}
	public void moveBackwards(){
		x -= getVector()[0] * getMomentum() * 0.5;
		y -= getVector()[1] * getMomentum() * 0.5;
		
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
	}
	public void update(){
		if(kbDur > 0){
			x += getKBVector()[0] * kbVelocity;
			y += getKBVector()[1] * kbVelocity;
			kbDur -= 1;
		} else {
			applyKnockback(0, 0, 0);
		}
		if(moving){
			move();
		} else if (backwards){
			moveBackwards();
		}
		tripOnUpdate();
	}
}
