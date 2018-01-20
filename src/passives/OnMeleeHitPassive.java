package passives;

import resources.OnHitAction;
import resources.Random;

public class OnMeleeHitPassive extends Passive{
	private int chance;
	
	public OnMeleeHitPassive(String n, int c){
		super(n);
		chance = c;
	}
	public void update(){
		OnHitAction a = new OnHitAction(){
			public void f(){
				if(Random.chance(chance)){
					applyEffect();
				}
			}
		};
		getRegisteredTo().getActionRegister().addOnMeleeHit(a);
	}
}
