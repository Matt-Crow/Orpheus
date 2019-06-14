package actives;

//import entities.Player;
import entities.ParticleType;
import javax.json.*;
import serialization.JsonSerialable;
import upgradables.UpgradableJsonUtil;

public class MeleeActive extends AbstractActive implements JsonSerialable{
	public MeleeActive(String n, int dmg){
		super(ActiveType.MELEE, n, 1, 1, 5, 0, dmg);
		setParticleType(ParticleType.SHEAR);
	}
    
    @Override
    public JsonObject serializeJson(){
        //no new fields are added in this subclass yet, so just refer to the superclass' method
        return super.serializeJson();
    }
    
    public static final MeleeActive deserializeJson(JsonObject obj){
        MeleeActive ret = new MeleeActive(
            UpgradableJsonUtil.getNameFrom(obj),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.DAMAGE)
        );
        ret.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        getTagsFrom(obj).stream().forEach(t->ret.addTag(t));
        ret.setParticleType(getParticleTypeFrom(obj));
        return ret;
    }
    
    @Override
	public MeleeActive copy(){
		MeleeActive copy = new MeleeActive(getName(), getBase(ActiveStatName.DAMAGE));
        copyTagsTo(copy);
        copy.setInflict(getInflict());
		return copy;
	}
	
    @Override
	public String getDescription(){
		String desc = getName() + ": \n"
				+ "The user performs a close range attack, \n"
				+ "dealing " + (int)getStatValue(ActiveStatName.DAMAGE) + " damage \n"
						+ "to whoever it hits. \n";
		if(getInflict().getSize() > 0){
			desc += getInflict().getStatusString();
		}
		
		return desc;
	}
	/*
	public void use(Player user){
		// Create a 45 degree angle, coming off of the user's angle
		Direction testDir = new Direction(user.getDir().getDegrees() + 45);
		// Go out 100 pixels
		int x = (int) (user.getX() + 100 * testDir.getXMod());
		int y = (int) (user.getY() + 100 * testDir.getYMod());
		
		Direction d = new Direction(user.getDir().getDegrees() - 90);
		
		setRegisteredProjectile(new SeedProjectile(x, y, d.getDegrees(), (int) getStatValue("Speed"), user, this));
		getRegisteredProjectile().getActionRegister().addOnHit(getStatusInfliction());
		if(getRegisteredProjectile().getAttack().getStatValue("Range") == 0){
			getRegisteredProjectile().terminate();
		}
		setToCooldown();
	}
	*/
}
