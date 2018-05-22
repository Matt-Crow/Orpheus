package passives;

import statuses.Status;

public class OnBeHitPassive extends AbstractPassive{
	
	public OnBeHitPassive(String n, int c, boolean targetsUser){
		super(PassiveType.ONBEHET, n, targetsUser);
		setChance(c);
	}
	
	public OnBeHitPassive copy(){
		OnBeHitPassive copy = new OnBeHitPassive(getName(), getChance(), getTargetsUser());
		for(Status s : getInflicts()){
			copy.addStatus(s);
		}
		return copy;
	}
	
	public void update(){
		getRegisteredTo().getActionRegister().addOnBeHit(getKey());
	}
}
