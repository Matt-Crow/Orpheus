package actives;

import initializers.Master;

public class ElementalActive extends AbstractActive{
	public ElementalActive(String n, int arc, int range, int speed, int aoe, int dmg){
		super(ActiveType.ELEMENTAL, n, arc, range, speed, aoe, dmg);
	}
	public ElementalActive copy(){
		ElementalActive copy = new ElementalActive(
				getName(), 
				getBase(ActiveStatName.ARC), 
				getBase(ActiveStatName.RANGE), 
				getBase(ActiveStatName.SPEED), 
				getBase(ActiveStatName.AOE), 
				getBase(ActiveStatName.DAMAGE));
		copy.setParticleType(getParticleType());
		
		return copy;
	}
	
	public void use(){
		super.use();
	}
	
	public String getDescription(){
		String desc = getName() + ": \n";
		if(getStatValue(ActiveStatName.RANGE) != 0){
			desc += "The user launches ";
			if(getStatValue(ActiveStatName.ARC) > 0){
				desc += "projectiles in a " + (int)getStatValue(ActiveStatName.ARC) + " degree arc, each traveling ";
			} else {
				desc += "a projectile, which travels ";
			}
			desc += "for " + (int)(getStatValue(ActiveStatName.RANGE) / Master.UNITSIZE) + " units, at " + (int)(getStatValue(ActiveStatName.SPEED) * Master.FPS / Master.UNITSIZE) + " units per second";
			
			if(getStatValue(ActiveStatName.AOE) != 0){
				desc += " before exploding in a " + (int)(getStatValue(ActiveStatName.AOE) / Master.UNITSIZE)+ " unit radius,"; 
			}
		} else {
			desc += "The user generates an explosion ";
			desc += "with a " + (int)(getStatValue(ActiveStatName.AOE) / Master.UNITSIZE) + " unit radius,";
		}
		desc += " dealing " + (int)getStatValue(ActiveStatName.DAMAGE) + " damage to enemies it hits. \n";
		desc += getCost() + " energy cost.";
		if(getInflict().getSize() > 0){
			desc += getInflict().getStatusString();
		}
		
		return desc;
	}
}
