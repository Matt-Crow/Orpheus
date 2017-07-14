package attacks;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import entities.Projectile;
import entities.Player;
import resources.OnUpdateAction;

public class Rupture extends ElementalAttack{
	public Rupture(){
		super("Rupture", 20, 100, 1000, 5, 0, 1, 1, 200);
	}
	public void use(Player user){
		super.use(user);
		Projectile p = getRegisteredProjectile();
		OnUpdateAction a = new OnUpdateAction();
		a.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
				switch(randomNum){
				case 0:
					p.turn("left");
					break;
				case 1:
					p.turn("right");
					break;
				}
			}
		});
		
		p.addOnUpdate(a);
	}
	/*
	public void update(){
		super.update();
		try{
			Projectile p = getRegisteredProjectile();
			int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
			switch(randomNum){
			case 0:
				p.turn("left");
				break;
			case 1:
				p.turn("right");
				break;
			}
		} catch(NullPointerException e){
			// your projectile doesn't exist
			return;
		}
	}
	*/
}
