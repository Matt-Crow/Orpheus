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
		boolean inRange = false;
		int r = Master.DETECTIONRANGE;
		// redo this
		/*
		Chunk[] chunks = Master.getCurrentBattle().getHost().getChunksContainedIn(appliedTo.getX() - r / 2, appliedTo.getY() - r / 2, r, r);
		for(int i = 0; i < chunks.length && !inRange; i++){
			Entity current = chunks[i].getHead();
			while(current.getHasChild()){
				current = current.getChild();
				if(current instanceof Player && !current.getTeam().equals(appliedTo.getTeam())){
					if(Coordinates.distanceBetween(current, appliedTo) <= Master.DETECTIONRANGE){
						inRange = true;
					}
				}
			}
		}*/
		
		return inRange;
	}
	
	public Player nearestEnemy(){
		Player nearest = new Player("ERROR");
		int distance = Master.DETECTIONRANGE + 1;
		
		
		int r = Master.DETECTIONRANGE;
		Chunk[] chunks = Master.getCurrentBattle().getHost().getChunksContainedIn(appliedTo.getX() - r / 2, appliedTo.getY() - r / 2, r, r);
		for(int i = 0; i < chunks.length; i++){
			Entity current = chunks[i].getHead();
			while(current.getHasChild()){
				current = current.getChild();
				if(current instanceof Player && !current.getTeam().equals(appliedTo.getTeam())){
					if(Coordinates.distanceBetween(current, appliedTo) < distance){
						nearest = (Player) current;
						distance = (int) Coordinates.distanceBetween(current, appliedTo);
					}
				}
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
