package world.builds.passives;

import orpheus.core.world.occupants.players.Player;
import world.events.EventListener;
import world.events.OnHitEvent;

public class OnBeHitPassive extends AbstractPassive implements EventListener<OnHitEvent> {
	/**
	 * Triggers once the user's hitbox intercepts 
	 * that of an enemy projectile
     * @param n the name of this passive
     * @param targetsUser whether or not statuses will be inflicted on the user or the hitter
	 */
	public OnBeHitPassive(String n, boolean targetsUser){
		super(n, targetsUser);
	}
	
    @Override
	public OnBeHitPassive copy(){
		OnBeHitPassive copy = new OnBeHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
    
    @Override
    public void init(){
        if(getUser() != null){
            getUser().getActionRegister().addOnBeHit(this); 
        }
    }
    
	@Override
    public void handle(OnHitEvent e) {
        if(getTargetsUser()){
            applyEffect((Player)e.getWasHit());
        } else {
            applyEffect((Player)e.getHitter());
        }
        trigger();
    }
    @Override
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "When the user is struck by an enemy projectile, \n";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "the attacker") + " with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
