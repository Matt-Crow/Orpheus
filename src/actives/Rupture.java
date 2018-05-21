package actives;
import actions.OnUpdateAction;
import entities.Projectile;
import resources.Random;

// find some GOOD way to implement this

public class Rupture extends ElementalActive{
	public Rupture(){
		super("Rupture", 4, 5, 5, 1, 0, 5);
	}
	public void use(){
		super.use();
		Projectile p = getLastUseProjectiles().get(0);
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				int randomNum = Random.choose(0, 4);
				switch(randomNum){
				case 0:
					p.turn("left");
					break;
				case 1:
					p.turn("right");
					break;
				}
			}
		};
		
		p.getActionRegister().addOnUpdate(a);
	}
}
