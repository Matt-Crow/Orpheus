package attacks;

import resources.OnHitAction;
import entities.Player;
import entities.Projectile;

public class Flurry extends MeleeAttack{
	private int recurCount;
	private boolean canRecur;
	
	public Flurry(){
		super("Flurry", 4, 1);
		recurCount = 0;
	}
	public void use(Player user){
		super.use(user);
		canRecur = true;
		Projectile p = getLastUseProjectiles().get(0);
		OnHitAction a = new OnHitAction(){
			public void f(){
				if(recurCount >= 2){
					recurCount = 0;
					canRecur = false;
					return;
				}
				if(canRecur){
					use(user);
					recurCount += 1;
				}
			}
		};
		
		p.getActionRegister().addOnHit(a);
	}
}
