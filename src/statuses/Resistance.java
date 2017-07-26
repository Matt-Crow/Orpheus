package statuses;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import entities.Player;
import resources.OnHitAction;

public class Resistance extends Status{
	public Resistance(int lv, int uses){
		super("Resistance", lv, uses);
	}
	public void inflictOn(Player p){
		OnHitAction a = new OnHitAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p.addFilter(1 - 0.25 * getIntensityLevel());
				use();
			}
		});
		p.addOnBeHit(a);
	}
}
