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
				getBaseArcLength(), 
				getBaseRange(), 
				getBaseProjectileSpeed(), 
				getBaseAreaOfEffect(), 
				getBaseDamage());
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
		if(getRange() == 0){
            desc.append(String.format("The user generates an explosion with a %d unit radius", getAOE() / Master.UNITSIZE));
        } else {
			desc.append("The user launches ");
			if(getArcLength() > 0){
				desc.append(
                    String.format(
                        "projectiles in a %d degree arc, each traveling ", 
                        getArcLength()
                    )
                );
			} else {
				desc.append("a projectile, which travels ");
			}
			desc.append(
                String.format(
                    "for %d units, at %d units per second", 
                    getRange() / Master.UNITSIZE,
                    getSpeed() * Master.FPS / Master.UNITSIZE
                )
            );
			
			if(getAOE() != 0){
				desc.append(String.format(" before exploding in a %d unit radius", getAOE() / Master.UNITSIZE)); 
			}
		}
        
		desc.append(String.format(" dealing %d damage to enemies it hits. \n", getDamage()));
		desc.append(String.format("%d energy cost. \n", getCost()));
		desc.append(getInflict().getStatusString());
		
        return desc.toString();
	}
}
