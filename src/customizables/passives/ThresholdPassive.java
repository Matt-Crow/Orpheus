package customizables.passives;

import actions.OnUpdateEvent;
import actions.OnUpdateListener;
import entities.Player;
import javax.json.JsonObject;
import serialization.JsonSerialable;
import customizables.CustomizableJsonUtil;
import util.Number;

/**
 * Triggers so long as the user is below a set percentage
 * of their maximum HP
 */
public class ThresholdPassive extends AbstractPassive implements JsonSerialable, OnUpdateListener{
	public ThresholdPassive(String n, int baseThresh){
		super(PassiveType.THRESHOLD, n, true);
		setStat(PassiveStatName.THRESHOLD, baseThresh);
	}
    @Override
	public ThresholdPassive copy(){
		ThresholdPassive copy = new ThresholdPassive(getName(), getBase(PassiveStatName.THRESHOLD));
		copyInflictTo(copy);
		return copy;
	}
    
    @Override
    public JsonObject serializeJson(){
        return super.serializeJson();
    }
    
    public static final ThresholdPassive deserializeJson(JsonObject obj){
        ThresholdPassive pass = new ThresholdPassive(
            CustomizableJsonUtil.getNameFrom(obj), 
            CustomizableJsonUtil.getStatBaseFrom(obj, PassiveStatName.THRESHOLD)
        );
        pass.setInflict(CustomizableJsonUtil.getStatusTableFrom(obj));
        
        return pass;
    }
    
	public void setStat(PassiveStatName n, int value){
		switch(n){
		case THRESHOLD:
			int base = Number.minMax(1, value, 3);
			double thresh = (1.0 / (6 - base)) * 100;
			/*
			 * 1: 1/5 (20%)
			 * 2: 1/4 (25%)
			 * 3: 1/3 (33%)
			 */
			addStat(PassiveStatName.THRESHOLD, base, thresh);
			break;
		}
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": ";
		desc += "When the user is at or below " + (int)getStatValue(PassiveStatName.THRESHOLD) + "% ";
		desc += "of their maximum HP, inflicts them with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}

    @Override
    public void init(){
        super.init();
        getUser().getActionRegister().addOnUpdate(this);
    }
    
    @Override
    public void trigger(OnUpdateEvent e) {
        if(((Player)e.getUpdated()).getLog().getHPPerc() <= getStatValue(PassiveStatName.THRESHOLD)){
            applyEffect((Player)e.getUpdated());
        }
    }
}
