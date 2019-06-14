package passives;

import javax.json.JsonObject;
import serialization.JsonSerialable;
import upgradables.UpgradableJsonUtil;

public class OnMeleeHitPassive extends AbstractPassive implements JsonSerialable{
	/*
	 * Same as onHitPassive, though only triggering off
	 * of melee hits
	 */
	public OnMeleeHitPassive(String n, boolean targetsUser){
		super(PassiveType.ONMELEEHIT, n, targetsUser);
	}
	
    @Override
	public OnMeleeHitPassive copy(){
		OnMeleeHitPassive copy = new OnMeleeHitPassive(getName(), getTargetsUser());
		copyInflictTo(copy);
		return copy;
	}
    
    @Override
    public JsonObject serializeJson(){
        return PassiveJsonUtil.serializeJson(this);
    }
    public static OnMeleeHitPassive deserializeJson(JsonObject obj){
        OnMeleeHitPassive pass = new OnMeleeHitPassive(
            UpgradableJsonUtil.getNameFrom(obj),
            PassiveJsonUtil.getTargetsUserFrom(obj)
        );
        pass.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        return pass;
    }
    
    @Override
	public void update(){
		getRegisteredTo().getActionRegister().addOnMeleeHit(getKey());
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": ";
		desc += "When the user strikes a target with a melee attack, ";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: ";
		desc += getInflict().getStatusString();
		return desc;
	}
}
