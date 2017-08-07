package statuses;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import entities.Player;
import resources.OnUpdateAction;

public class Rush extends Status{
	public Rush(int lv, int dur){
		super("Rush", lv, dur);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p.applySpeedFilter(1 + 0.2 * getIntensityLevel());
				use();
			}
		});
		p.addOnUpdate(a);
	}
}
