package passives;

import serialization.JsonSerialable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class OnMeleeHitPassive extends AbstractPassive implements JsonSerialable{
	/*
	 * Same as onHitPassive, though only triggering off
	 * of melee hits
	 */
	public OnMeleeHitPassive(String n, boolean targetsUser){
		super(PassiveType.ONMELEEHIT, n, targetsUser);
	}
	
	public OnMeleeHitPassive copy(){
		OnMeleeHitPassive copy = new OnMeleeHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
	public void update(){
		getRegisteredTo().getActionRegister().addOnMeleeHit(getKey());
	}
	public String getDescription(){
		String desc = getName() + ": ";
		desc += "When the user strikes a target with a melee attack, ";
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
    public static OnMeleeHitPassive deserializeJson(JsonObject obj){
        OnMeleeHitPassive pass = new OnMeleeHitPassive(
            getNameFrom(obj),
            getTargetsUserFrom(obj)
        );
        pass.setInflict(getStatusTableFrom(obj));
        return pass;
    }
}
