package ai;

import entities.Entity;
import entities.Player;
import initializers.Master;
import resources.Random;
import resources.Direction;

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
		int x1 = appliedTo.getCoords().getX();
		int x2 = latched.getCoords().getX();
		int y1 = appliedTo.getCoords().getY();
		int y2 = latched.getCoords().getY();
		
		Direction d = Direction.getDegreeByLengths(x1, y1, x2, y2);
		appliedTo.turnToward(d);
	}
	
	public boolean checkIfPlayerInSightRange(){
		boolean inRange = false;
		
		Player current = appliedTo.getTeam().getEnemy().getHead();
		while(current.getHasChild()){
			current = (Player)current.getChild();
			if(current.getCoords().distanceBetween(appliedTo.getCoords()) <= Master.DETECTIONRANGE){
				inRange = true;
			}
		}
		
		return inRange;
	}
	
	public Player nearestEnemy(){
		Player nearest = new Player("ERROR");
		int distance = Master.DETECTIONRANGE + 1;
		Player current = appliedTo.getTeam().getEnemy().getHead();
		while(current.getHasChild()){
			current = (Player)current.getChild();
			if(current.getCoords().distanceBetween(appliedTo.getCoords()) < distance){
				nearest = current;
				distance = (int) current.getCoords().distanceBetween(appliedTo.getCoords());
			}
		}
		if(nearest.getName() == "ERROR"){
			throw new NullPointerException();
		}
		return nearest;
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
