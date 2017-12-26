package attacks;

import entities.Projectile;
import entities.Player;
import entities.ParticleType;
import resources.CustomColors;
import resources.OnUpdateAction;

public class Tornado extends ElementalAttack{
	public Tornado(){
		super("Tornado", 5, 5, 5, 2, 0, 3);
		setParticleType(ParticleType.BEAM);
		setColorBlend(CustomColors.airColors);
	}
	
	public void use(Player user){
		super.use(user);
		Projectile p = getRegisteredProjectile();
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.turn("left");
			}
		};
		p.getActionRegister().addOnUpdate(a);
	}
}
