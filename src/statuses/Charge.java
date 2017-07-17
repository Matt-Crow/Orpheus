package statuses;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import entities.Player;
import resources.OnUpdateAction;

public class Charge extends Status{
	public Charge(int lv, int dur){
		super("Charge", lv, dur);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p.getEnergyLog().gainEnergy((int) (2.5 * getIntensityLevel()));
				use();
			}
		});
		p.addOnUpdate(a);
	}
}
