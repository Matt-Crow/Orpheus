package customizables.passives;

import actions.OnHitEvent;
import actions.OnHitListener;
import entities.AbstractPlayer;

public class OnMeleeHitPassive extends AbstractPassive implements OnHitListener{
	/*
	 * Same as onHitPassive, though only triggering off
	 * of melee hits
	 */
	public OnMeleeHitPassive(String n, boolean targetsUser){
		super(PassiveType.ONMELEEHIT, n, targetsUser);
	}
	
    @Override
	public OnMeleeHitPassive copy(){
		OnMeleeHitPassive copy = new OnMeleeHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
    
    @Override
	public void init(){
        super.init();
        if(getUser() != null){
            getUser().getActionRegister().addOnMeleeHit(this);
        }
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": ";
		desc += "When the user strikes a target with a melee attack, ";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}

    @Override
    public void trigger(OnHitEvent e) {
        applyEffect((AbstractPlayer)((getTargetsUser()) ? e.getHitter() : e.getWasHit()));
    }
}
