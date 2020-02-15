package customizables.actives;

import controllers.Master;

public class ElementalActive extends AbstractActive{
	public ElementalActive(String n, int arc, int range, int speed, int aoe, int dmg){
		super(ActiveType.ELEMENTAL, n, arc, range, speed, aoe, dmg);
	}
    @Override
	public ElementalActive copy(){
		ElementalActive copy = new ElementalActive(
				getName(), 
				getBase(ActiveStatName.ARC), 
				getBase(ActiveStatName.RANGE), 
				getBase(ActiveStatName.SPEED), 
				getBase(ActiveStatName.AOE), 
				getBase(ActiveStatName.DAMAGE));
		copy.setParticleType(getParticleType());
		copyTagsTo(copy);
        copy.setColors(getColors());
        copy.setInflict(getInflict());
		return copy;
	}
	
    @Override
	public String getDescription(){
        StringBuilder desc = new StringBuilder();
		
        desc
            .append(getName())
            .append(": \n");
		if(getStatValue(ActiveStatName.RANGE) == 0){
            desc.append(String.format("The user generates an explosion with a %d unit radius", (int)(getStatValue(ActiveStatName.AOE) / Master.UNITSIZE)));
        } else {
			desc.append("The user launches ");
			if(getStatValue(ActiveStatName.ARC) > 0){
				desc.append(
                    String.format(
                        "projectiles in a %d degree arc, each traveling ", 
                        (int)getStatValue(ActiveStatName.ARC)
                    )
                );
			} else {
				desc.append("a projectile, which travels ");
			}
			desc.append(
                String.format(
                    "for %d units, at %d units per second", 
                    (int)(getStatValue(ActiveStatName.RANGE) / Master.UNITSIZE),
                    (int)(getStatValue(ActiveStatName.SPEED) * Master.FPS / Master.UNITSIZE)
                )
            );
			
			if(getStatValue(ActiveStatName.AOE) != 0){
				desc.append(String.format(" before exploding in a %d unit radius", (int)(getStatValue(ActiveStatName.AOE) / Master.UNITSIZE))); 
			}
		}
        
		desc.append(String.format(" dealing %d damage to enemies it hits. \n", (int)getStatValue(ActiveStatName.DAMAGE)));
		desc.append(String.format("%d energy cost. \n", getCost()));
		desc.append(getInflict().getStatusString());
		
        return desc.toString();
	}
}
