package customizables.passives;

import events.OnHitEvent;
import events.OnHitListener;
import entities.AbstractPlayer;

public class OnHitPassive extends AbstractPassive implements OnHitListener{
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
            //for when doInit is called by Customizers
            getUser().getActionRegister().addOnHit(this);
        }
    }
    
    @Override
    public void trigger(OnHitEvent e) {
        if(getTargetsUser()){
            applyEffect((AbstractPlayer)e.getHitter());
        } else {
            applyEffect((AbstractPlayer)e.getWasHit());
        }
        trigger();
    }
	
    @Override
	public String getDescription(){
		String desc = getName() + ": ";
		desc += "When the user performs an attack that successfully hits an enemy, ";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
