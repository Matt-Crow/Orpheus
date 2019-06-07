package passives;

import PsuedoJson.JsonSerialable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class OnBeHitPassive extends AbstractPassive implements JsonSerialable{
	/**
	 * Triggers once the user's hitbox intercepts 
	 * that of an enemy projectile
	 */
	public OnBeHitPassive(String n, boolean targetsUser){
		super(PassiveType.ONBEHIT, n, targetsUser);
	}
	
    @Override
	public OnBeHitPassive copy(){
		OnBeHitPassive copy = new OnBeHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
	
    @Override
	public void update(){
		getRegisteredTo().getActionRegister().addOnBeHit(getKey());
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "When the user is struck by an enemy projectile, \n";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: \n";
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
        b.add("type", "on be hit passive");
        return b.build();
    }
    
    public static OnBeHitPassive deserializeJson(JsonObject obj){
        OnBeHitPassive obh = new OnBeHitPassive(
            getNameFrom(obj),
            getTargetsUserFrom(obj)
        );
        obh.setInflict(getStatusTableFrom(obj));
        return obh;
    }
}
