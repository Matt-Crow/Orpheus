package ai;

import entities.Player;
import actions.OnHitTrip;
import actions.OnHitKey;

public class PlayerAI extends AI{
	private Player registered;
	
	public PlayerAI(Player p){
		super(p);
		registered = p;
	}
	public void setToAttack(){
		setMode("attack");
	}
	public void pursue(){
		super.pursue();
		// check if in range
		if(registered.getCoords().distanceBetween(getLatched().getCoords()) <= 100){
			setToAttack();
		}
	}
	public void attack(){
		if(getLatched().getShouldTerminate()){
			setMode("wander");
			return;
		}
		
		// check if out of range
		if(getApplied().getCoords().distanceBetween(getLatched().getCoords()) >= 100){
			setMode("pursue");
		} else {
			turnToLatch();
			registered.useMeleeAttack();
			registered.setMoving(false);
		}
	}
	public void update(){
		super.update();
		if(isEnabled()){
			if(getMode() == "attack"){
				attack();
			}
		}
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				latch((Player)t.getHitter());
			}
		};
		getApplied().getActionRegister().addOnBeHit(a);
	}
}
