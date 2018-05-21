package actives;

public class ElementalActive extends Active{
	public ElementalActive(ElementalActiveBlueprint b){
		super(b.getName(), b.getCost(), b.getCooldown(), b.getRange(), b.getSpeed(), b.getAoe(), b.getDmg());
	}
	/*
	public ElementalActive(String n, int energyCost, int cooldown, int range, int speed, int aoe, int dmg){
		super(n, energyCost, cooldown, range, speed, aoe, dmg);
	}*/
}
