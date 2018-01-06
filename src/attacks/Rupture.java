package attacks;
import entities.Projectile;
import entities.Player;
import resources.OnUpdateAction;
import resources.Random;

public class Rupture extends ElementalAttack{
	public Rupture(){
		super("Rupture", 4, 5, 5, 1, 0, 5);
	}
	public void use(Player user){
		super.use(user);
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
