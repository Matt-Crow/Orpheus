package passives;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import resources.OnHitAction;
import resources.Random;

public class OnHitPassive extends Passive{
	private int chance;
	
	public OnHitPassive(String n, int c){
		super(n, "on hit");
		chance = c;
	}
	public void update(){
		OnHitAction a = new OnHitAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(Random.chance(chance)){
					applyEffect();
				}
			}
		});
		getPlayer().addOnBeHit(a);
	}
}
