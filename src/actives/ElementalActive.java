package actives;

import initializers.Master;

public class ElementalActive extends AbstractActive{
	public ElementalActive(String n, int arc, int range, int speed, int aoe, int dmg){
		super(ActiveType.ELEMENTAL, n, arc, range, speed, aoe, dmg);
	}
	public ElementalActive copy(){
		ElementalActive copy = new ElementalActive(getName(), getBase("Arc"), getBase("Range"), getBase("Speed"), getBase("AOE"), getBase("Damage"));
		copy.setParticleType(getParticleType());
		
		return copy;
	}
	
	public void use(){
		super.use();
	}
	
	public String getDescription(){
		String desc = getName() + ": \n";
		if(getStatValue(ActiveStat.RANGE) != 0){
			desc += "The user launches ";
			if(getStatValue(ActiveStat.ARC) > 0){
				desc += "projectiles in a " + (int)getStatValue(ActiveStat.ARC) + " degree arc, each traveling ";
			} else {
				desc += "a projectile, which travels ";
			}
			desc += "for " + (int)(getStatValue(ActiveStat.RANGE) / Master.UNITSIZE) + " units, at " + (int)(getStatValue(ActiveStat.SPEED) * Master.FPS / Master.UNITSIZE) + " units per second";
			
			if(getStatValue(ActiveStat.AOE) != 0){
				desc += " before exploding in a " + (int)(getStatValue(ActiveStat.AOE) / Master.UNITSIZE)+ " unit radius,"; 
			}
		} else {
			desc += "The user generates an explosion ";
			desc += "with a " + (int)(getStatValue(ActiveStat.AOE) / Master.UNITSIZE) + " unit radius,";
		}
		desc += " dealing " + (int)getStatValue(ActiveStat.DAMAGE) + " damage to enemies it hits. \n";
		desc += getCost() + " energy cost.";
		if(getInflict().getSize() > 0){
			desc += getInflict().getStatusString();
		}
		
		return desc;
	}
}
