package passives;

import actions.OnHitEvent;
import actions.OnHitListener;
import entities.Player;
import javax.json.JsonObject;
import serialization.JsonSerialable;
import upgradables.UpgradableJsonUtil;

public class OnBeHitPassive extends AbstractPassive implements JsonSerialable, OnHitListener{
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
    public JsonObject serializeJson(){
        return super.serializeJson();
    }
    
    public static OnBeHitPassive deserializeJson(JsonObject obj){
        OnBeHitPassive obh = new OnBeHitPassive(
            UpgradableJsonUtil.getNameFrom(obj),
            getTargetsUserFrom(obj)
        );
        obh.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        return obh;
    }
    
    @Override
    public void init(){
        super.init();
        getRegisteredTo().getActionRegister().addOnBeHit(this);
    }
    
	@Override
    public void trigger(OnHitEvent e) {
        if(getTargetsUser()){
            applyEffect((Player)e.getWasHit());
        } else {
            applyEffect((Player)e.getHitter());
        }
    }
    @Override
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "When the user is struck by an enemy projectile, \n";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "the attacker") + " with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
