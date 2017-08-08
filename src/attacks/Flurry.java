package attacks;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import resources.OnHitAction;
import entities.Player;
import entities.Projectile;

public class Flurry extends MeleeAttack{
	private int recurCount;
	
	public Flurry(){
		super("Flurry", 40, 35);
		recurCount = 0;
	}
	public void use(Player user){
		super.use(user);
		Projectile p = getRegisteredProjectile();
		OnHitAction a = new OnHitAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(recurCount >= 5){
					recurCount = 0;
					return;
				}
				use(user);
				recurCount += 1;
			}
		});
		p.addOnHit(a);
	}
}
