package ai;

import entities.Player;
import actions.*;
import resources.Coordinates;

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
		if(Coordinates.distanceBetween(registered, getLatched()) <= 100){
			setToAttack();
		}
	}
	public void attack(){
		if(getLatched().getShouldTerminate()){
			setMode("wander");
			return;
		}
		
		// check if out of range
		if(Coordinates.distanceBetween(getApplied(), getLatched()) >= 100){
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
		OnHitListener a = new OnHitListener(){
            @Override
			public void actionPerformed(OnHitEvent t){
				latch((Player)t.getHitter());
			}
		};
		getApplied().getActionRegister().addOnBeHit(a);
	}
}
