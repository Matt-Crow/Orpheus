package statuses;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import entities.Player;
import resources.OnUpdateAction;

public class Healing extends Status{
	public Healing(int lv){
		super("Healing", lv, 1);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p.getLog().healPerc(100 / (6 - getIntensityLevel()));
				use();
			}
		});
	}
}
