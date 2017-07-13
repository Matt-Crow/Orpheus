package passives;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import resources.OnHitAction;
import resources.Random;

public class OnBeHitPassive extends Passive{
	private int chance;
	
	public OnBeHitPassive(String n, int c){
		super(n, "on be hit");
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
