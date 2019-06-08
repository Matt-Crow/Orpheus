package actives;

//import entities.Player;
import PsuedoJson.JsonSerialable;
import PsuedoJson.PsuedoJsonObject;
import entities.ParticleType;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class MeleeActive extends AbstractActive implements JsonSerialable{
	public MeleeActive(String n, int dmg){
		super(ActiveType.MELEE, n, 1, 1, 5, 0, dmg);
		setParticleType(ParticleType.SHEAR);
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

    @Override
    public PsuedoJsonObject getPsuedoJson() {
        PsuedoJsonObject j = new PsuedoJsonObject(getName());
        j.addPair("Type", this.getType().toString());
        j.addPair(ActiveStatName.DAMAGE.toString(), getBase(ActiveStatName.DAMAGE) + "");
        j.addPair("Tags", getTagPsuedoJson());
        return j;
    }
    
    @Override
    public JsonObject serializeJson(){
        JsonObject obj = super.serializeJson();
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        return b.build();
    }
    
    public static final MeleeActive deserializeJson(JsonObject obj){
        MeleeActive ret = new MeleeActive(
            getNameFrom(obj),
            getStatBaseFrom(obj, ActiveStatName.DAMAGE.toString())
        );
        ret.setInflict(getStatusTableFrom(obj));
        ret.addTags(getTagsFrom(obj));
        ret.setParticleType(getParticleTypeFrom(obj));
        return ret;
    }
    
}
