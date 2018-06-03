package ai;

import entities.Entity;
import entities.Player;
import initializers.Master;
import resources.Random;
import resources.Coordinates;

public class AI {
	private Entity appliedTo;
	private String mode;
	private int wanderDistance;
	private int distanceWandered;
	private Player latched;
	private boolean enabled;
	
	public AI(Entity p){
		appliedTo = p;
		enabled = false;
	}
	
	public Entity getApplied(){
		return appliedTo;
	}
	public void latch(Player p){
		mode = "pursue";
		latched = p;
	}
	public Player getLatched(){
		return latched;
	}
	public void setMode(String s){
		mode = s;
	}
	public String getMode(){
		return mode;
	}
	public void enable(){
		enabled = true;
		setToWander();
	}
	public void disable(){
		enabled = false;
	}
	public boolean isEnabled(){
		return enabled;
	}

	public void setToWander(){
		mode = "wander";
		wanderDistance = Random.choose(100, 500);
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
	public void wander(){
		appliedTo.setMoving(true);
		if(distanceWandered >= wanderDistance){
			setToWander();
		}
	}
	public void pursue(){
		turnToLatch();
		appliedTo.setMoving(true);
	}
	
	public void turnToLatch(){
		appliedTo.setFocus(latched);
	}
	
	public boolean checkIfPlayerInSightRange(){
		Player nearest = nearestEnemy();
		return Coordinates.distanceBetween(appliedTo, nearest) <= Master.DETECTIONRANGE;
	}
	
	public Player nearestEnemy(){
		return appliedTo.getTeam().getEnemy().nearestPlayerTo(appliedTo.getX(), appliedTo.getY());
	}
	public void latchOntoNearest(){
		latch(nearestEnemy());
	}
	
	public void update(){
		if(enabled){
			if(mode == "wander"){
				wander();
				distanceWandered += appliedTo.getMomentum();
				
				if(checkIfPlayerInSightRange()){
					latchOntoNearest();
				}
			} else if(mode == "pursue"){
				pursue();
			}
		}
	}
}
