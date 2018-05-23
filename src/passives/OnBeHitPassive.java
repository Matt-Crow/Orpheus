package passives;

import statuses.StatusTable;

public class OnBeHitPassive extends AbstractPassive{
	
	public OnBeHitPassive(String n, int c, boolean targetsUser){
		super(PassiveType.ONBEHET, n, targetsUser);
		setChance(c);
	}
	
	public OnBeHitPassive copy(){
		OnBeHitPassive copy = new OnBeHitPassive(getName(), getChance(), getTargetsUser());
		StatusTable orig = getInflict();
		for(int i = 0; i < orig.getSize(); i++){
			copy.addStatus(orig.getNameAt(i), orig.getIntensityAt(i), orig.getDurationAt(i));
		}
		return copy;
	}
	
	public void update(){
		getRegisteredTo().getActionRegister().addOnBeHit(getKey());
	}
}
