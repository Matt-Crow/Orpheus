package passives;

import statuses.Status;

public class OnMeleeHitPassive extends AbstractPassive{
	
	public OnMeleeHitPassive(String n, int c, boolean targetsUser){
		super(PassiveType.ONMELEEHIT, n, targetsUser);
		setChance(c);
	}
	
	public OnMeleeHitPassive copy(){
		OnMeleeHitPassive copy = new OnMeleeHitPassive(getName(), getChance(), getTargetsUser());
		for(Status s : getInflicts()){
			copy.addStatus(s);
		}
		return copy;
	}
	public void update(){
		getRegisteredTo().getActionRegister().addOnMeleeHit(getKey());
	}
}
