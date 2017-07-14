package statuses;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import entities.Player;
import resources.OnUpdateAction;

public class Stun extends Status{
	public Stun(int lv, int dur){
		super("Stun", lv, dur);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p.setMoving(false);
				use();
			}
		});
		p.addOnUpdate(a);
	}
}
