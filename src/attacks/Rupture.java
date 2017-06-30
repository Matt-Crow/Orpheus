package attacks;
import java.util.concurrent.ThreadLocalRandom;
import entities.Projectile;

public class Rupture extends ElementalAttack{
	public Rupture(){
		super("Rupture", 20, 100, 1000, 5, 0, 1, 1, 200);
	}
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
}
