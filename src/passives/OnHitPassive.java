package passives;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import serialization.JsonSerialable;
import upgradables.UpgradableJsonUtil;

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
    public JsonObject serializeJson(){
        return PassiveJsonUtil.serializeJson(this);
    }
    
    public static OnHitPassive deserializeJson(JsonObject obj){
        OnHitPassive ret = new OnHitPassive(
            UpgradableJsonUtil.getNameFrom(obj),
            PassiveJsonUtil.getTargetsUserFrom(obj)
        );
        ret.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        return ret;
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
}
