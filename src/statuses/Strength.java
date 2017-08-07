package statuses;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import entities.Player;
import resources.OnHitAction;

public class Strength extends Status{
	public Strength(int lv, int uses){
		super("Strength", lv, uses);
	}
	public void inflictOn(Player p){
		OnHitAction a = new OnHitAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				a.getTarget().getLog().logPercentageDamage(3.5 * getIntensityLevel());
				a.getTarget().applyKnockback(p.getDirNum(), 10, (int) (3.5 * getIntensityLevel()));
				use();
			}
		});
		p.addOnMeleeHit(a);
	}
}
