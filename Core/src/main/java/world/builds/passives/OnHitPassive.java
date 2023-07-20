package world.builds.passives;

import orpheus.core.world.occupants.players.Player;
import world.events.EventListener;
import world.events.OnHitEvent;

public class OnHitPassive extends AbstractPassive implements EventListener<OnHitEvent> {
	/**
	 * Triggers upon a projectile registered to the
	 * user colliding with an enemy player
     * 
     * @param n the name of this passive
     * @param targetsUser whether or not statuses will be inflicted on the user or the hitter
	 */
	public OnHitPassive(String n, boolean targetsUser){
		super(n, targetsUser);
	}
	
    @Override
	public OnHitPassive copy(){
		OnHitPassive copy = new OnHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
    
    @Override
    public void init(){
        if(getUser() != null){
            //for when init is called by Customizers
            getUser().getActionRegister().addOnHit(this);
        }
    }
    
    @Override
    public void handle(OnHitEvent e) {
        if(getTargetsUser()){
            applyEffect((Player)e.getHitter());
        } else {
            applyEffect((Player)e.getWasHit());
        }
        trigger();
    }
	
    @Override
	public String getDescription(){
		var desc = "When the user performs an attack that successfully hits an enemy, ";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
