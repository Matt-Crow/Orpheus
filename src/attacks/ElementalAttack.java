package attacks;

public class ElementalAttack extends Attack{
	public ElementalAttack(String n, int energyCost, int chargeup, int cooldown, double chargeScale, int range, int offset, int aoe, int areaScale, int distanceScale, int dmg){
		super(n, energyCost, chargeup, cooldown, chargeScale, range, offset, aoe, areaScale, distanceScale, dmg);
	}
}
