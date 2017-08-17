package statuses;

import entities.Player;
import resources.OnHitAction;

public class Strength extends Status{
	public Strength(int lv, int uses){
		super("Strength", lv, uses);
	}
	public void inflictOn(Player p){
		OnHitAction a = new OnHitAction(){
			public void f(){
				getHit().getLog().logPercentageDamage(3.5 * getIntensityLevel());
				getHit().applyKnockback(p.getDirNum(), 10, (int) (3.5 * getIntensityLevel()));
				use();
			}
		};
		p.addOnMeleeHit(a);
	}
}
