package customizables.passives;

import actions.OnHitEvent;
import actions.OnHitListener;
import entities.AbstractPlayer;

public class OnHitPassive extends AbstractPassive implements OnHitListener{
	/**
	 * Triggers upon a projectile registered to the
	 * user colliding with an enemy player
	 */
	
	public OnHitPassive(String n, boolean targetsUser){
		super(PassiveType.ONHIT, n, targetsUser);
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
