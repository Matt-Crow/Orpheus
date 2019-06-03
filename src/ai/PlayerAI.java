package ai;

import entities.Player;
import actions.*;
import util.Coordinates;

public class PlayerAI extends AI{
	private Player registered;
	
	public PlayerAI(Player p){
		super(p);
		registered = p;
	}
	public void setToAttack(){
		setMode(AiMode.ATTACK);
    }
    
    @Override
	public void pursue(){
		//super.pursue();
		// check if in range
		if(Coordinates.distanceBetween(registered, getLatched()) <= 100){
			setToAttack();
		} else {
            registered.setPath(
                registered.getWorld().findPath(
                    registered.getX(), 
                    registered.getY(), 
                    getLatched().getX(), 
                    getLatched().getY()
                )
            );
        }
	}
	public void attack(){
		if(getLatched().getShouldTerminate()){
			setToWander();
			return;
		}
		
		// check if out of range
		if(Coordinates.distanceBetween(getApplied(), getLatched()) >= 100){
			setMode(AiMode.PURSUE);
		} else {
			turnToLatch();
			registered.useMeleeAttack();
			registered.setMoving(false);
		}
	}
    @Override
	public void update(){
		super.update();
		if(isEnabled()){
			if(getMode() == AiMode.ATTACK){
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
