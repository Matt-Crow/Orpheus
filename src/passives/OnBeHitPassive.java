package passives;

public class OnBeHitPassive extends AbstractPassive{
	/**
	 * Triggers once the user's hitbox intercepts 
	 * that of an enemy projectile
	 */
	public OnBeHitPassive(String n, int c, boolean targetsUser){
		super(PassiveType.ONBEHET, n, targetsUser);
		setChance(c);
	}
	
	public OnBeHitPassive copy(){
		OnBeHitPassive copy = new OnBeHitPassive(getName(), getChance(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
	
	public void update(){
		getRegisteredTo().getActionRegister().addOnBeHit(getKey());
	}
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "When the user is struck by an enemy projectile, \n";
		desc += "there is a " + getChance() + "% chance \n";
		desc += "that the " + ((getTargetsUser()) ? "user" : "target") + " will be inflicted with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
