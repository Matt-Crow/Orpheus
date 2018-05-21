package actives;

public class ElementalActiveBlueprint extends AbstractActiveBlueprint{
	public ElementalActiveBlueprint(String n, int cost, int cd, int range, int speed, int aoe, int dmg){
		super(ActiveType.ELEMENTAL, n, cost, cd, range, speed, aoe, dmg);
	}
}
