package customizables.passives;

import actions.OnHitEvent;
import actions.OnHitListener;
import entities.Player;
import javax.json.JsonObject;
import serialization.JsonSerialable;
import customizables.CustomizableJsonUtil;

public class OnHitPassive extends AbstractPassive implements JsonSerialable, OnHitListener{
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
        return super.serializeJson();
    }
    
    public static OnHitPassive deserializeJson(JsonObject obj){
        OnHitPassive ret = new OnHitPassive(
            CustomizableJsonUtil.getNameFrom(obj),
            getTargetsUserFrom(obj)
        );
        ret.setInflict(CustomizableJsonUtil.getStatusTableFrom(obj));
        return ret;
    }
    
    @Override
    public void init(){
        super.init();
        getUser().getActionRegister().addOnHit(this);
    }
    
    @Override
    public void trigger(OnHitEvent e) {
        if(getTargetsUser()){
            applyEffect((Player)e.getHitter());
        } else {
            applyEffect((Player)e.getWasHit());
        }
    }
	
    @Override
	public String getDescription(){
		String desc = getName() + ": ";
		desc += "When the user performs an attack that successfully hits an enemy, ";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
