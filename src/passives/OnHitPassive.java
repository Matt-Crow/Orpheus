package passives;

import resources.OnHitAction;
import resources.Random;

public class OnHitPassive extends Passive{
	private int chance;
	
	public OnHitPassive(String n, int c){
		super(n, "on hit");
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
		getPlayer().addOnHit(a);
	}
}
