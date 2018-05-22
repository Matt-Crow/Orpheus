package passives;

import statuses.Status;

public class OnHitPassive extends AbstractPassive{
	
	public OnHitPassive(String n, int c, boolean targetsUser){
		super(PassiveType.ONHIT, n, targetsUser);
		setChance(c);
	}
	
	public OnHitPassive copy(){
		OnHitPassive copy = new OnHitPassive(getName(), getChance(), getTargetsUser());
		for(Status s : getInflicts()){
			copy.addStatus(s);
		}
		return copy;
	}
	
	public void update(){
		getRegisteredTo().getActionRegister().addOnHit(getKey());
	}
}
