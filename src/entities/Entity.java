package entities;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import java.util.ArrayList;
import resources.OnHitAction;
import resources.Op;
import resources.Direction;

public class Entity {
	private int x;
	private int y;
	private int dirNum;
	private int momentum;
	private double speedFilter;
	private boolean moving;
	
	private ArrayList<OnHitAction> onHitRegister;
	private ArrayList<OnHitAction> onBeHitRegister;
	private ArrayList<OnHitAction> onMeleeHitRegister;
	private ArrayList<OnHitAction> onBeMeleeHitRegister;
	private ArrayList<AbstractAction> onUpdateRegister;
	
	public Entity(int xCoord, int yCoord, int directionNumber, int m){
		x = xCoord;
		y = yCoord;
		dirNum = directionNumber;
		momentum = m;
		speedFilter = 1.0;
		moving = false;
		
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
	public void setDirNum(int dir){
		dirNum = dir;
	}
	public void setMoving(boolean m){
		moving = m;
	}
	public void applySpeedFilter(double f){
		speedFilter *= f;
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
	public void addOnUpdate(AbstractAction a){
		onUpdateRegister.add(a);
	}
	public int[] getVector(){
		return Direction.directions[dirNum];
	}
	public int getDirNum(){
		return dirNum;
	}
	public int getMomentum(){
		return (int) (momentum * speedFilter);
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
			a.setTarget(hit);
			a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null){});
		}
	}
	public void tripOnBeHit(Player hitBy){
		for(OnHitAction a : onBeHitRegister){
			a.setTarget(hitBy);
			a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null){});
		}
	}
	public void tripOnMeleeHit(Player hit){
		for(OnHitAction a : onMeleeHitRegister){
			a.setTarget(hit);
			a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null){});
		}
		for(OnHitAction a : onHitRegister){
			a.setTarget(hit);
			a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null){});
		}
	}
	public void tripOnBeMeleeHit(Player hitBy){
		for(OnHitAction a : onBeMeleeHitRegister){
			a.setTarget(hitBy);
			a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null){});
		}
		for(OnHitAction a : onBeHitRegister){
			a.setTarget(hitBy);
			a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null){});
		}
	}
	public void tripOnUpdate(){
		for(AbstractAction a : onUpdateRegister){
			a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null){});
		}
	}
	public void turn(String dir){
		if(dir == "left"){
			dirNum -= 1;
		} else {
			dirNum += 1;
		}
		if(dirNum < 0){
			dirNum = 7;
		} else if(dirNum > 7){
			dirNum = 0;
		}
	}
	// add collisions
	public void move(){
		x += getVector()[0] * getMomentum();
		y += getVector()[1] * getMomentum();
		
		if(x < 0){
			x = 0;
		} else if(x > 2000){
			x = 2000;
		}
		
		if(y < 0){
			y = 0;
		} else if(y > 2000){
			y = 2000;
		}
	}
	public void update(){
		if(moving){
			move();
		}
		tripOnUpdate();
	}
}
