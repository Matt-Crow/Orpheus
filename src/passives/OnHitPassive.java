package passives;

public class OnHitPassive extends AbstractPassive{
	/**
	 * Triggers upon a projectile registered to the
	 * user colliding with an enemy player
	 */
	
	public OnHitPassive(String n, boolean targetsUser){
		super(PassiveType.ONHIT, n, targetsUser);
	}
	
	public OnHitPassive copy(){
		OnHitPassive copy = new OnHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
	
	public void update(){
		getRegisteredTo().getActionRegister().addOnHit(getKey());
	}
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "When the user performs an attack that successfully hits an enemy, \n";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
