package actives;

public class ElementalActive extends AbstractActive{
	public ElementalActive(String n, int arc, int range, int speed, int aoe, int dmg){
		super(ActiveType.ELEMENTAL, n, arc, range, speed, aoe, dmg);
	}
	public ElementalActive copy(){
		ElementalActive copy = new ElementalActive(getName(), getBase("Arc"), getBase("Range"), getBase("Speed"), getBase("AOE"), getBase("Damage"));
		copy.setParticleType(getParticleType());
		copy.setInflict(getInflict());
		
		return copy;
	}
	
	public void use(){
		super.use();
	}
	
	public String getDescription(){
		String desc = getName() + ": \n";
		if(getStatValue("Range") != 0){
			desc += "The user launches ";
			if(getStatValue("Arc") > 0){
				desc += "projectiles in a " + getStatValue("Arc") + " degree arc, \n each traveling ";
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
		desc += "dealing " + getStatValue("Damage") + " damage to enemies it hits \n";
		if(getInflict().getSize() > 0){
			desc += getInflict().getStatusString();
		}
		
		return desc;
	}
}
