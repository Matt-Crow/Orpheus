package passives;

public class OnMeleeHitPassive extends AbstractPassive{
	/*
	 * Same as onHitPassive, though only triggering off
	 * of melee hits
	 */
	public OnMeleeHitPassive(String n, int c, boolean targetsUser){
		super(PassiveType.ONMELEEHIT, n, targetsUser);
		setChance(c);
	}
	
	public OnMeleeHitPassive copy(){
		OnMeleeHitPassive copy = new OnMeleeHitPassive(getName(), getChance(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
	public void update(){
		getRegisteredTo().getActionRegister().addOnMeleeHit(getKey());
	}
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "When the user strikes a target with a melee attack, \n";
		desc += "there is a " + getChance() + "% chance \n";
		desc += "that the " + ((getTargetsUser()) ? "user" : "target") + " will be inflicted with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
