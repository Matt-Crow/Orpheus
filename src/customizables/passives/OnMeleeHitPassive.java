package customizables.passives;

import actions.OnHitEvent;
import actions.OnHitListener;
import entities.Player;
import javax.json.JsonObject;
import serialization.JsonSerialable;
import customizables.CustomizableJsonUtil;

public class OnMeleeHitPassive extends AbstractPassive implements JsonSerialable, OnHitListener{
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
        return super.serializeJson();
    }
    public static OnMeleeHitPassive deserializeJson(JsonObject obj){
        OnMeleeHitPassive pass = new OnMeleeHitPassive(
            CustomizableJsonUtil.getNameFrom(obj),
            getTargetsUserFrom(obj)
        );
        pass.setInflict(CustomizableJsonUtil.getStatusTableFrom(obj));
        return pass;
    }
    
    @Override
	public void init(){
        super.init();
		getUser().getActionRegister().addOnMeleeHit(this);
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": ";
		desc += "When the user strikes a target with a melee attack, ";
		desc += "inflicts " + ((getTargetsUser()) ? "user" : "target") + " with: ";
		desc += getInflict().getStatusString();
		return desc;
	}

    @Override
    public void trigger(OnHitEvent e) {
        applyEffect((Player)((getTargetsUser()) ? e.getHitter() : e.getWasHit()));
    }
}
