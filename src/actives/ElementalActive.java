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
	public int getArcDegrees(){
		return arcLength;
	}
	public int getProjCount(){
		return projectileCount;
	}
	public void use(){
		super.use();
		spawnArc(arcLength, projectileCount);
	}
	public ElementalActive copy(){
		int[] s = getStatCode();
		ElementalActive copy = new ElementalActive(getName(), s[0], s[1], s[2], s[3], s[4], s[5]);
		if(getTracking()){
			copy.enableTracking();
		}
		copy.setParticleType(getParticleType());
		copy.setColorBlend(getColors());
		copy.setInflict(getInflict());
		
		return copy;
	}
}
