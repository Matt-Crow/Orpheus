package attacks;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import resources.OnHitAction;
import entities.Player;
import entities.Projectile;

public class Flurry extends MeleeAttack{
	private int recurCount;
	private boolean canRecur;
	
	public Flurry(){
		super("Flurry", 40, 35);
		recurCount = 0;
	}
	public void use(Player user){
		super.use(user);
		canRecur = true;
		Projectile p = getRegisteredProjectile();
		OnHitAction a = new OnHitAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(recurCount >= 2){
					recurCount = 0;
					canRecur = false;
					return;
				}
				if(canRecur){
					use(user);
					recurCount += 1;
				}
			}
		});
		p.addOnHit(a);
	}
}
