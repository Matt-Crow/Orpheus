package passives;

import actions.OnHitTrip;
import entities.Player;
import actions.OnHitKey;
import resources.Random;
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
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				if(Random.chance(getChance())){
					if(getTargetsUser()){
						applyEffect(getRegisteredTo());
					} else {
						applyEffect((Player)t.getHit());
					}
				}
			}
		};
		getRegisteredTo().getActionRegister().addOnMeleeHit(a);
	}
}
