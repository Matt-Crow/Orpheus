package passives;

import actions.OnHitTrip;
import entities.Player;
import actions.OnHitKey;
import resources.Random;
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
		
		getRegisteredTo().getActionRegister().addOnBeHit(a);
	}
}
