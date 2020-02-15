package customizables.actives;

import entities.ParticleType;

public class MeleeActive extends AbstractActive{
	public MeleeActive(String n, int dmg){
		super(ActiveType.MELEE, n, 1, 1, 5, 0, dmg);
		setParticleType(ParticleType.SHEAR);
	}
    
    @Override
	public MeleeActive copy(){
		MeleeActive copy = new MeleeActive(getName(), getBase(ActiveStatName.DAMAGE));
        copyTagsTo(copy);
        copy.setColors(getColors());
        copy.setInflict(getInflict());
		return copy;
	}
	
    @Override
	public String getDescription(){
		String desc = getName() + ": \n"
				+ "The user performs a close range attack, \n"
				+ "dealing " + (int)getStatValue(ActiveStatName.DAMAGE) + " damage \n"
						+ "to whoever it hits. \n";
        desc += getInflict().getStatusString();
		
		return desc;
	}
}
