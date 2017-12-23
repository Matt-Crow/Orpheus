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
	
	public AI(Entity p){
		appliedTo = p;
	}
	
	public Entity getApplied(){
		return appliedTo;
	}
	
	public Player getLatched(){
		return latched;
	}
	
	public void setMode(String s){
		mode = s;
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
				if(checkIfPlayerInSightRange()){
					appliedTo.getAI().latchOntoNearest();
				}
			}
		};
		appliedTo.getActionRegister().addOnBeHit(a);
	}
}
