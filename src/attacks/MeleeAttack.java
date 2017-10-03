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
		
		// Create a 45 degree angle, coming off of the user's angle
		Direction testDir = new Direction(user.getDir().getDegrees() - 45);
		// Go out 50 pixels
		int x = (int) (user.getX() - 50 * testDir.getXMod());
		int y = (int) (user.getY() + 50 * testDir.getYMod());
		
		Direction d = new Direction(user.getDir().getDegrees() - 90);
		
		setRegisteredProjectile(new SeedProjectile(x, y, d.getDegrees(), (int) getStatValue("Speed"), user, this));
		
		//setRegisteredProjectile(new SeedProjectile(user.getX(), user.getY(), user.getDir().getDegrees(), (int) getStatValue("Speed"), user, this));
		getRegisteredProjectile().addOnHit(getStatusInfliction());
		if(getRegisteredProjectile().getAttack().getStatValue("Range") == 0){
			getRegisteredProjectile().terminate();
		}
		setToCooldown();
	}
}
