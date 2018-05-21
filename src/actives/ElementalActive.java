package actives;

public class ElementalActive extends AbstractActive{
	public ElementalActive(String n, int energyCost, int cooldown, int range, int speed, int aoe, int dmg){
		super(ActiveType.ELEMENTAL, n, energyCost, cooldown, range, speed, aoe, dmg);
	}
	public ElementalActive copy(){
		//TODO: make this pass by value
		return this;
	}
}
