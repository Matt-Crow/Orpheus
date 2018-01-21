package actions;

import entities.Entity;

public class OnHitTrip extends AbstractTrip{
	private Entity wasHit;
	private Entity hitter;
	
	public OnHitTrip(Entity hit, Entity hitBy){
		wasHit = hit;
		hitter = hitBy;
	}
	public Entity getHit(){
		return wasHit;
	}
	public Entity getHitter(){
		return hitter;
	}
}
