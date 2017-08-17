package attacks;
import entities.Projectile;
import entities.Player;
import resources.OnUpdateAction;
import resources.Random;

public class Rupture extends ElementalAttack{
	public Rupture(){
		super("Rupture", 20, 100, 1000, 5, 0, 1, 1, 200);
	}
	public void use(Player user){
		super.use(user);
		Projectile p = getRegisteredProjectile();
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
		
		p.addOnUpdate(a);
	}
}
