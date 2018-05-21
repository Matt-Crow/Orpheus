package actives;

public class ElementalActive extends AbstractActive{
	private int arcLength;
	private int projectileCount;
	
	public ElementalActive(String n, int energyCost, int cooldown, int range, int speed, int aoe, int dmg){
		super(ActiveType.ELEMENTAL, n, energyCost, cooldown, range, speed, aoe, dmg);
		arcLength = 1;
		projectileCount = 1;
	}
	public void setArc(int degrees, int count){
		arcLength = degrees;
		projectileCount = count;
	}
	public void use(){
		super.use();
		spawnArc(arcLength, projectileCount);
	}
	public ElementalActive copy(){
		//TODO: make this pass by value
		return this;
	}
}
