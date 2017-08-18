package ai;

import resources.OnHitAction;
import entities.Player;
import resources.Coordinates;
import resources.Random;
import resources.Direction;

public class AI {
	private Player appliedTo;
	
	private String mode;
	private int wanderDistance;
	private int distanceWandered;
	private Player latched;
	
	public AI(Player p){
		appliedTo = p;
	}
	
	public void setToWander(){
		mode = "wander";
		wanderDistance = Random.choose(100, 1000);
		distanceWandered = 0;
		switch(Random.choose(0, 1)){
		case 0:
			appliedTo.turn("left");
			break;
		case 1:
			appliedTo.turn("right");
			break;
		}
	}
	public void latch(Player p){
		mode = "pursue";
		latched = p;
	}
	public void setToAttack(){
		mode = "attack";
	}
	
	public void wander(){
		appliedTo.setMoving(true);
		if(distanceWandered >= wanderDistance){
			setToWander();
		}
	}
	public void pursue(){
		// check if in range
		if(appliedTo.getCoords().distanceBetween(latched.getCoords()) <= 100){
			setToAttack();
			return;
		}
		
		turnToLatch();
		appliedTo.setMoving(true);
	}
	public void attack(){
		if(latched.getShouldTerminate()){
			mode = "wander";
			return;
		}
		
		// check if out of range
		if(appliedTo.getCoords().distanceBetween(latched.getCoords()) >= 100){
			mode = "pursue";
			return;
		}
		turnToLatch();
		appliedTo.useMeleeAttack();
		appliedTo.setMoving(false);
	}
	
	// improve this
	public void turnToLatch(){
		boolean isAbove = false;
		boolean isBelow = false;
		boolean isLeft = false;
		boolean isRight = false;
		int x1 = appliedTo.getCoords().getX();
		int x2 = latched.getCoords().getX();
		int y1 = appliedTo.getCoords().getY();
		int y2 = latched.getCoords().getY();
		
		if(x1 > x2){
			isLeft = true;
		} else if(x1 < x2){
			isRight = true;
		}
		
		if(y1 > y2){
			isAbove = true;
		} else if(y1 < y2){
			isBelow = true;
		}
		Double turnTo;
		if(isAbove){
			if(isLeft){
				turnTo = 0.75;
			} else if(isRight){
				turnTo = 0.25;
			} else {
				turnTo = 0.5;
			}
		} else if(isBelow){
			if(isLeft){
				turnTo = 1.25;
			} else if(isRight){
				turnTo = 1.75;
			} else {
				turnTo = 1.5;
			}
		} else {
			if(isLeft){
				turnTo = 1.0;
			} else {
				turnTo = 0.0;
			}
		}
		
		appliedTo.turnToward(new Direction(turnTo));
	}
	public boolean checkIfPlayerInSightRange(){
		for(Coordinates c : appliedTo.getTeam().getEnemy().getAllCoords()){
			if(c.distanceBetween(appliedTo.getCoords()) <= 500){
				return true;
			}
		}
		return false;
	}
	public Player nearestEnemy(){
		Coordinates nearest = new Coordinates(0, 0);
		int distance = 999999;
		for(Coordinates c : appliedTo.getTeam().getEnemy().getAllCoords()){
			if(c.distanceBetween(appliedTo.getCoords()) < distance){
				nearest = c;
				distance = (int) c.distanceBetween(appliedTo.getCoords());
			}
		}
		return (Player) nearest.getRegistered();
	}
	public void latchOntoNearest(){
		latch(nearestEnemy());
	}
	
	public void update(){
		if(mode == "wander"){
			wander();
			distanceWandered += appliedTo.getMomentum();
			
			if(checkIfPlayerInSightRange()){
				latchOntoNearest();
			}
		} else if(mode == "pursue"){
			pursue();
		} else if(mode == "attack"){
			attack();
		}
		OnHitAction a = new OnHitAction(){
			public void f(){
				appliedTo.getAI().latchOntoNearest();
			}
		};
		appliedTo.addOnBeHit(a);
	}
}
