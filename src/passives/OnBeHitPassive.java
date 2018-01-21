package passives;

import actions.OnHitTrip;
import actions.OnHitKey;
import resources.Random;

public class OnBeHitPassive extends Passive{
	private int chance;
	
	public OnBeHitPassive(String n, int c){
		super(n);
		chance = c;
	}
	public void update(){
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				if(Random.chance(chance)){
					//TODO: make this choose who to apply to
					applyEffect();
				}
			}
		};
		
		getRegisteredTo().getActionRegister().addOnBeHit(a);
	}
}
