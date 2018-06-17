package passives;

public class OnBeHitPassive extends AbstractPassive{
	/**
	 * Triggers once the user's hitbox intercepts 
	 * that of an enemy projectile
	 */
	public OnBeHitPassive(String n, boolean targetsUser){
		super(PassiveType.ONBEHIT, n, targetsUser);
	}
	
	public OnBeHitPassive copy(){
		OnBeHitPassive copy = new OnBeHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
	
	public void update(){
		getRegisteredTo().getActionRegister().addOnBeHit(getKey());
	}
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "When the user is struck by an enemy projectile, \n";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
