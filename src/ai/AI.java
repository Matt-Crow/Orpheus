package ai;

import resources.OnHitAction;
import entities.Entity;
import entities.Player;
import initializers.Master;
import resources.Coordinates;
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
	
	//give each entity own speed
	public void wander(){
		appliedTo.setMomentum(Master.MAXPLAYERSPEED);
		if(distanceWandered >= wanderDistance){
			setToWander();
		}
	}
	
	
	public void pursue(){
		turnToLatch();
		appliedTo.setMomentum(Master.MAXPLAYERSPEED);
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
		for(Coordinates c : appliedTo.getTeam().getEnemy().getAllCoords()){
			if(c.distanceBetween(appliedTo.getCoords()) <= Master.DETECTIONRANGE){
				return true;
			}
		}
		return false;
	}
	public Player nearestEnemy(){
		Coordinates nearest = new Coordinates(0, 0);
		int distance = Master.DETECTIONRANGE;
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
			OnHitAction a = new OnHitAction(){
				public void f(){
					if(checkIfPlayerInSightRange()){
						appliedTo.getEntityAI().latchOntoNearest();
					}
				}
			};
			appliedTo.getActionRegister().addOnBeHit(a);
		}
	}
}
