package statuses;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import entities.Player;
import resources.OnHitAction;
import resources.Op;

public class Resistance extends Status{
	public Resistance(int lv, int uses){
		super("Resistance", lv, uses);
	}
	public void inflictOn(Player p){
		OnHitAction a = new OnHitAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p.addFilter(1 - 0.2 * getIntensityLevel());
				use();
			}
		});
		p.addOnBeHit(a);
	}
}
