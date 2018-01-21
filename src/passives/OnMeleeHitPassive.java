package passives;

import actions.OnHitTrip;
import actions.OnHitKey;
import resources.Random;

public class OnMeleeHitPassive extends Passive{
	private int chance;
	
	public OnMeleeHitPassive(String n, int c){
		super(n);
		chance = c;
	}
	public void update(){
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				if(Random.chance(chance)){
					applyEffect();
				}
			}
		};
		getRegisteredTo().getActionRegister().addOnMeleeHit(a);
	}
}
