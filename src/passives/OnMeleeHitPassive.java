package passives;

public class OnMeleeHitPassive extends AbstractPassive{
	/*
	 * Same as onHitPassive, though only triggering off
	 * of melee hits
	 */
	public OnMeleeHitPassive(String n, boolean targetsUser){
		super(PassiveType.ONMELEEHIT, n, targetsUser);
	}
	
	public OnMeleeHitPassive copy(){
		OnMeleeHitPassive copy = new OnMeleeHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
	public void update(){
		getRegisteredTo().getActionRegister().addOnMeleeHit(getKey());
	}
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "When the user strikes a target with a melee attack, \n";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
