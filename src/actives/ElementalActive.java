package actives;

public class ElementalActive extends AbstractActive{
	private int arcLength; // the length of the arc (in degrees) of projectiles generated by this active when used
	private int projCount; // the number of projectiles that make up said arc
	
	public ElementalActive(String n, int energyCost, int cooldown, int range, int speed, int aoe, int dmg){
		super(ActiveType.ELEMENTAL, n, energyCost, cooldown, range, speed, aoe, dmg);
		arcLength = 1;
		projCount = 1;
	}
	public ElementalActive copy(){
		ActiveStatBaseValues b = getBases();
		ElementalActive copy = new ElementalActive(getName(), b.getCost(), b.getCooldown(), b.getSpeed(), b.getRange(), b.getAoe(), b.getDmg());
		copy.setArc(getArcDegrees(), getProjCount());
		copy.setParticleType(getParticleType());
		copy.setColorBlend(getColors());
		copy.setInflict(getInflict());
		
		return copy;
	}
	
	// arc management
	public void setArc(int degrees, int count){
		arcLength = degrees;
		projCount = count;
	}
	public int getArcDegrees(){
		return arcLength;
	}
	public int getProjCount(){
		return projCount;
	}
	
	public void use(){
		super.use();
		spawnArc(arcLength, projCount);
	}
	
	public String getDescription(){
		String desc = getName() + ": \n";
		if(getStatValue("Range") != 0){
			desc += "The user launches ";
			if(projCount > 1){
				desc += projCount + " projectiles in a " + arcLength + " degree arc, \n each traveling ";
			} else {
				desc += "a projectile, which travels ";
			}
			desc += "for " + getStatValue("Range") + " units \n";
			
			if(getStatValue("AOE") != 0){
				desc += "before exploding in a " + getStatValue("AOE") + " unit radius, \n"; 
			}
		} else {
			desc += "The user generates an explosion \n";
			desc += "with a " + getStatValue("AOE") + " unit radius, \n";
		}
		desc += "dealing " + getStatValue("Damage") + " damage to enemies it hits";
		if(getInflict().getSize() > 0){
			desc += "\n and having " + getStatusString();
		}
		
		return desc;
	}
}
