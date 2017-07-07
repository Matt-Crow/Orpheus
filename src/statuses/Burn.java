package statuses;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import entities.Player;
import resources.OnUpdateAction;
import resources.Op;

public class Burn extends Status{
	public Burn(int lv, int uses){
		super("Burn", lv, uses);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p.addFilter(1 + 0.1 * getIntensityLevel());
				use();
				Op.add("Uses left " + getUsesLeft());
				Op.dp();
			}
		});
		p.addOnUpdate(a);
	}
}
