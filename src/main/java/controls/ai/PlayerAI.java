package controls.ai;

import world.events.OnHitEvent;
import world.entities.AbstractPlayer;
import util.Settings;
import java.io.Serializable;

/**
 * The PlayerAI class is used to control non-human-controlled
 * Players. It supports extendable behaviors, as seen in AbstractBehavior.
 * 
 * In previous versions of Orpheus, all Entities would have AI,
 * but would be turned off for everything but fake players and tracking projectiles.
 * Since tracking projectiles don't feel like they fit with the game anymore,
 * I've removed the base AI class.
 * 
 * @author Matt Crow
 */
public class PlayerAI implements Serializable{
	private final AbstractPlayer appliedTo;
    private AbstractBehavior currentBehavior;
	
	public PlayerAI(AbstractPlayer p){
		appliedTo = p;
        currentBehavior = null;
	}
    
    public void init(){
        currentBehavior = null;
        if(!Settings.DISABLEALLAI){
            currentBehavior = new WanderBehavior(appliedTo);
            appliedTo.getActionRegister().addOnBeHit((OnHitEvent e) -> {
                currentBehavior = new PursueBehavior(appliedTo, (AbstractPlayer)e.getHitter());
            });
        }
    }
    
	public void update(){
        if(
            !Settings.DISABLEALLAI &&
            !appliedTo.getTeam().getEnemy().isDefeated() &&
            currentBehavior != null
        ){
            currentBehavior = currentBehavior.update();
        }
	}
}
