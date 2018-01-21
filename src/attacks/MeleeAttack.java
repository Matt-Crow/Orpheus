package attacks;

//import entities.Player;
import entities.ParticleType;
import graphics.CustomColors;

public class MeleeAttack extends Attack{
	public MeleeAttack(String n, int cooldown, int dmg){
		super(n, 0, cooldown, 1, 5, 0, dmg);
		setParticleColor(CustomColors.silver);
		setParticleType(ParticleType.SHEAR);
	}
	/*
	public void use(Player user){
		// Create a 45 degree angle, coming off of the user's angle
		Direction testDir = new Direction(user.getDir().getDegrees() + 45);
		// Go out 100 pixels
		int x = (int) (user.getX() + 100 * testDir.getXMod());
		int y = (int) (user.getY() + 100 * testDir.getYMod());
		
		Direction d = new Direction(user.getDir().getDegrees() - 90);
		
		setRegisteredProjectile(new SeedProjectile(x, y, d.getDegrees(), (int) getStatValue("Speed"), user, this));
		getRegisteredProjectile().getActionRegister().addOnHit(getStatusInfliction());
		if(getRegisteredProjectile().getAttack().getStatValue("Range") == 0){
			getRegisteredProjectile().terminate();
		}
		setToCooldown();
	}
	*/
}
