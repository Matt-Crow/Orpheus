package passives;

import statuses.StatusTable;

public class OnHitPassive extends AbstractPassive{
	
	public OnHitPassive(String n, int c, boolean targetsUser){
		super(PassiveType.ONHIT, n, targetsUser);
		setChance(c);
	}
	
	public OnHitPassive copy(){
		OnHitPassive copy = new OnHitPassive(getName(), getChance(), getTargetsUser());
		StatusTable orig = getInflict();
		for(int i = 0; i < orig.getSize(); i++){
			copy.addStatus(orig.getNameAt(i), orig.getIntensityAt(i), orig.getDurationAt(i));
		}
		return copy;
	}
	
	public void update(){
		getRegisteredTo().getActionRegister().addOnHit(getKey());
	}
}
