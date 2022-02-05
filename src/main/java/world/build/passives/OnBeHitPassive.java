package world.build.passives;

import world.events.OnHitEvent;
import world.events.OnHitListener;
import world.entities.AbstractPlayer;

public class OnBeHitPassive extends AbstractPassive implements OnHitListener{
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
    public void trigger(OnHitEvent e) {
        if(getTargetsUser()){
            applyEffect((AbstractPlayer)e.getWasHit());
        } else {
            applyEffect((AbstractPlayer)e.getHitter());
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
