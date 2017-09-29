package attacks;

import entities.Player;
import entities.SeedProjectile;
import resources.Direction;
import resources.Op;

public class MeleeAttack extends Attack{
	public MeleeAttack(String n, int cooldown, int dmg){
		super(n, 0, cooldown, 100, 5, 0, 0, 1, dmg);
		setType("melee");
	}
	public void use(Player user){
		Direction d = new Direction(user.getDir().getDegrees() - 90);
		// why is this not working?
		int x = (int) (user.getX() - 100 * d.getXMod());
		int y = (int) (user.getY() - 100 * d.getYMod());
		Op.add(d.getXMod());
		Op.add(d.getYMod());
		Op.dp();
		setRegisteredProjectile(new SeedProjectile(x, y, d, (int) getStatValue("Speed"), user, this));
		getRegisteredProjectile().addOnHit(getStatusInfliction());
		if(getRegisteredProjectile().getAttack().getStatValue("Range") == 0){
			getRegisteredProjectile().terminate();
		}
		setToCooldown();
	}
}
