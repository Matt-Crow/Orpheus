package statuses;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import entities.Player;
import resources.OnUpdateAction;

public class Regeneration extends Status{
	public Regeneration(int lv, int dur){
		super("Regeneration", lv, dur);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p.getLog().healPerc(0.125 * getIntensityLevel());
				use();
			}
		});
		p.addOnUpdate(a);
	}
}
