package attacks;

public class ElementalAttack extends Attack{
	public ElementalAttack(String n, int energyCost, int cooldown, int range, int speed, int aoe, int areaScale, int distanceScale, int dmg){
		super(n, energyCost, cooldown, range, speed, aoe, areaScale, distanceScale, dmg);
		setType("elemental");
	}
}
