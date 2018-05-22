package passives;

import actions.OnHitTrip;
import entities.Player;
import actions.OnHitKey;
import resources.Random;
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
		getRegisteredTo().getActionRegister().addOnHit(a);
	}
}
