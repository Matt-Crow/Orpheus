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
		
		//...
		
		// move to them
		boolean isAbove;
		boolean isBelow;
		boolean isLeft;
		boolean isRight;
		int x1 = appliedTo.getCoords().getX();
		int x2 = latched.getCoords().getX();
		int y1 = appliedTo.getCoords().getY();
		int y2 = latched.getCoords().getY();
		
		if(x1 > x2){
			isLeft = true;
			isRight = false;
		} else if(x1 < x2){
			isLeft = false;
			isRight = true;
		}
		
		if(y1 > y2){
			isAbove = true;
			isBelow = false;
		} else if(y1 < y2){
			isAbove = false;
			isBelow = true;
		}
		
		// add turntoward here
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
