package customizables.actives;

import entities.ParticleType;

public class MeleeActive extends AbstractActive{
	public MeleeActive(String n, int dmg){
		super(ActiveType.MELEE, n, 1, 1, 5, 0, dmg);
		setParticleType(ParticleType.SHEAR);
	}
    
    @Override
	public MeleeActive copy(){
		MeleeActive copy = new MeleeActive(getName(), getBaseDamage());
        copyTagsTo(copy);
        copy.setColors(getColors());
        copyInflictTo(copy);
        
		return copy;
	}
	
    @Override
	public String getDescription(){
		String desc = getName() + ": \n"
				+ "The user performs a close range attack, \n"
				+ "dealing " + getDamage() + " damage \n"
						+ "to whoever it hits. \n";
        desc += getInflict().getStatusString();
		
		return desc;
	}
}
