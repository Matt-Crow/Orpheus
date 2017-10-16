package ai;

import resources.OnHitAction;
import entities.Player;
import initializers.Master;
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
		appliedTo.setMomentum(Master.MAXPLAYERSPEED);
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
		appliedTo.setMomentum(Master.MAXPLAYERSPEED);
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
		appliedTo.setMomentum(0);
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
				appliedTo.getAI().latchOntoNearest();
			}
		};
		appliedTo.getActionRegister().addOnBeHit(a);
	}
}
