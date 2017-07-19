package ai;

import entities.Player;
import resources.Coordinates;
import resources.Op;
import resources.Random;

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
		Op.add(appliedTo.getName() + " is now pursuing " + p.getName());
		Op.dp();
		latched = p;
	}
	
	public void wander(){
		appliedTo.setMoving(true);
		if(distanceWandered >= wanderDistance){
			setToWander();
		}
	}
	// work here
	public void pursue(){
		// check if in range
		if(appliedTo.getCoords().distanceBetween(latched.getCoords()) <= 100){
			/*
			appliedTo.disableAI();
			appliedTo.setMoving(false);
			latched.disableAI();
			latched.setMoving(false);
			*/
			return;
		}
		
		// move to them
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
		String turnTo;
		if(isAbove){
			if(isLeft){
				turnTo = "Northwest";
			} else if(isRight){
				turnTo = "Northeast";
			} else {
				turnTo = "North";
			}
		} else if(isBelow){
			if(isLeft){
				turnTo = "Southwest";
			} else if(isRight){
				turnTo = "Southeast";
			} else {
				turnTo = "South";
			}
		} else {
			if(isLeft){
				turnTo = "West";
			} else {
				turnTo = "East";
			}
		}
		
		appliedTo.turnToward(turnTo);
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
		return appliedTo.getTeam().getEnemy().getPlayerByCoords(nearest);
	}
	public void update(){
		if(mode == "wander"){
			wander();
			distanceWandered += appliedTo.getMomentum();
			
			if(checkIfPlayerInSightRange()){
				latch(nearestEnemy());
			}
		} else if(mode == "pursue"){
			pursue();
		}
	}
}
