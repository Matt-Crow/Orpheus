package passives;

import serialization.JsonSerialable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class OnHitPassive extends AbstractPassive implements JsonSerialable{
	/**
	 * Triggers upon a projectile registered to the
	 * user colliding with an enemy player
	 */
	
	public OnHitPassive(String n, boolean targetsUser){
		super(PassiveType.ONHIT, n, targetsUser);
	}
	
    @Override
	public OnHitPassive copy(){
		OnHitPassive copy = new OnHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
	
    @Override
	public void update(){
		getRegisteredTo().getActionRegister().addOnHit(getKey());
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": ";
		desc += "When the user performs an attack that successfully hits an enemy, ";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: ";
		desc += getInflict().getStatusString();
		return desc;
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
    
    public static OnHitPassive deserializeJson(JsonObject obj){
        OnHitPassive ret = new OnHitPassive(
            getNameFrom(obj),
            getTargetsUserFrom(obj)
        );
        ret.setInflict(getStatusTableFrom(obj));
        return ret;
    }
}
