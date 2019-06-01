package ai;

import entities.Entity;
import entities.Player;
import initializers.Master;
import util.Random;
import util.Coordinates;

public class AI {
	private final Entity appliedTo;
	private AiMode mode;
	private int wanderDistance;
	private int distanceWandered;
	private Player latched;
	private boolean enabled;
	
	public AI(Entity p){
		appliedTo = p;
        mode = AiMode.NONE;
		enabled = false;
	}
	
	public Entity getApplied(){
		return appliedTo;
	}
	public void latch(Player p){
		mode = AiMode.PURSUE;
		latched = p;
	}
	public Player getLatched(){
		return latched;
	}
	public void setMode(AiMode m){
		mode = m;
	}
	public AiMode getMode(){
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
		mode = AiMode.WANDER;
		wanderDistance = Random.choose(100, 500);
		distanceWandered = 0;
		
        appliedTo.getDir().turnClockwise(Random.choose(0, 360));
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
		if(enabled && !appliedTo.getTeam().getEnemy().isDefeated()){
			if(mode == AiMode.WANDER){
				wander();
				distanceWandered += appliedTo.getMomentum();
				
				if(checkIfPlayerInSightRange()){
					latchOntoNearest();
				}
			} else if(mode == AiMode.PURSUE){
				pursue();
			}
		}
	}
}
