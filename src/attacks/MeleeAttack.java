package attacks;

public class MeleeAttack extends Attack{
	public MeleeAttack(String n, int cooldown, int dmg){
		super(n, 0, cooldown, 100, 5, 0, 0, 1, dmg);
	}
}
