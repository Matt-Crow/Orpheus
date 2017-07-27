package statuses;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import entities.Player;
import resources.OnHitAction;
import resources.Random;

public class Daze extends Status{
	public Daze(int lv, int dur){
		super("Daze", lv, dur);
	}
	public void inflictOn(Player p){
		OnHitAction a = new OnHitAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p.setDirNum(Random.choose(0, 7));
				use();
			}
		});
	}
}
