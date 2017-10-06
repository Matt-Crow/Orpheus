package passives;

import resources.OnHitAction;
import resources.Random;

public class OnBeHitPassive extends Passive{
	private int chance;
	
	public OnBeHitPassive(String n, int c){
		super(n, "on be hit");
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
		
		getPlayer().getActionRegister().addOnBeHit(a);
	}
}
