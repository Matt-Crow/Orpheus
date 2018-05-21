package actives;

import entities.Projectile;
import graphics.CustomColors;
import actions.OnUpdateAction;
import entities.ParticleType;

public class Tornado extends ElementalAttack{
	public Tornado(){
		super("Tornado", 5, 5, 5, 2, 0, 3);
		setParticleType(ParticleType.BEAM);
		setColorBlend(CustomColors.airColors);
	}
	
	public void use(){
		super.use();
		Projectile p = getLastUseProjectiles().get(0);
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.turn("left");
			}
		};
		p.getActionRegister().addOnUpdate(a);
	}
}
