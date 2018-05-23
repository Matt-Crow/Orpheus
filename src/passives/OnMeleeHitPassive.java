package passives;

import statuses.StatusTable;

public class OnMeleeHitPassive extends AbstractPassive{
	
	public OnMeleeHitPassive(String n, int c, boolean targetsUser){
		super(PassiveType.ONMELEEHIT, n, targetsUser);
		setChance(c);
	}
	
	public OnMeleeHitPassive copy(){
		OnMeleeHitPassive copy = new OnMeleeHitPassive(getName(), getChance(), getTargetsUser());
		StatusTable orig = getInflict();
		for(int i = 0; i < orig.getSize(); i++){
			copy.addStatus(orig.getNameAt(i), orig.getIntensityAt(i), orig.getDurationAt(i));
		}
		return copy;
	}
	public void update(){
		getRegisteredTo().getActionRegister().addOnMeleeHit(getKey());
	}
}
