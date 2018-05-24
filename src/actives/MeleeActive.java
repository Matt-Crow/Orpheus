package actives;

//import entities.Player;
import entities.ParticleType;
import graphics.CustomColors;
import initializers.Master;

public class MeleeActive extends AbstractActive{
	public MeleeActive(String n, int cooldown, int dmg){
		super(ActiveType.MELEE, n, 0, cooldown, 1, 5, 0, dmg);
		setParticleColor(CustomColors.silver);
		setParticleType(ParticleType.SHEAR);
	}
	public MeleeActive copy(){
		MeleeActive copy = new MeleeActive(getName(), getBases().getCooldown(), getBases().getDmg());
		return copy;
	}
	
	public String getDescription(){
		String desc = getName() + ": \n"
				+ "The user performs a close range attack, \n"
				+ "dealing " + (int)getStatValue("Damage") + " damage \n"
						+ "to whoever it hits. \n"
						+ Master.framesToSeconds((int)getStatValue("Cooldown")) + " second cooldown.";
		
		
		return desc;
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
