package statuses;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import entities.Player;
import resources.OnHitAction;
import resources.Op;

public class Strength extends Status{
	public Strength(int lv, int uses){
		super("Strength", lv, -1, uses);
	}
	public void inflictOn(Player p){
		OnHitAction a = new OnHitAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				a.getTarget().logPercentageDamage(3.5 * getIntensityLevel());
				use();
				Op.add("Uses left " + getUsesLeft());
				Op.dp();
			}
		});
		p.addOnHit(a);
	}
}
