package ai;

import entities.Player;
import resources.Random;

public class AI {
	private Player appliedTo;
	
	private String mode;
	private int wanderDistance;
	private int distanceWandered;
	
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
	
	public void wander(){
		appliedTo.setMoving(true);
		if(distanceWandered >= wanderDistance){
			setToWander();
		}
	}
	public void update(){
		if(mode == "wander"){
			wander();
			distanceWandered += appliedTo.getMomentum();
		}
	}
}
