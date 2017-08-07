package statuses;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import entities.Player;
import resources.OnUpdateAction;

public class Burn extends Status{
	public Burn(int lv, int uses){
		super("Burn", lv, uses);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p.getLog().applyFilter(1 + 0.25 * getIntensityLevel());
				use();
			}
		});
		p.addOnUpdate(a);
	}
}
