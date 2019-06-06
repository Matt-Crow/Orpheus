package passives;

import PsuedoJson.JsonSerialable;
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
	
	public OnHitPassive copy(){
		OnHitPassive copy = new OnHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
	
	public void update(){
		getRegisteredTo().getActionRegister().addOnHit(getKey());
	}
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
        b.add("type", "on hit passive");
        return b.build();
    }
    
    public static Object deserializeJson(JsonObject obj){
        return obj;
    }
}
