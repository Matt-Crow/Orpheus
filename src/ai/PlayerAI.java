package ai;

import entities.Player;

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
			return;
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
			return;
		}
		turnToLatch();
		registered.useMeleeAttack();
		registered.setMomentum(0);
	}
}
