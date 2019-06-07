package passives;

import PsuedoJson.JsonSerialable;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import util.Number;

/**
 * Triggers so long as the user is below a set percentage
 * of their maximum HP
 */
public class ThresholdPassive extends AbstractPassive implements JsonSerialable{
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
	public void setStat(PassiveStatName n, int value){
		switch(n){
		case THRESHOLD:
			int base = Number.minMax(1, value, 5);
			double thresh = (1.0 / (8 - base)) * 100;
			/*
			 * 1: 1/7 (14%)
			 * 2: 1/6 (17%)
			 * 3: 1/5 (20%)
			 * 4: 1/4 (25%)
			 * 5: 1/3 (33%)
			 */
			addStat(PassiveStatName.THRESHOLD, base, thresh);
			break;
		}
	}
    @Override
	public void update(){
		if(getRegisteredTo().getLog().getHPPerc() <= getStatValue(PassiveStatName.THRESHOLD)){
			applyEffect(getRegisteredTo());
		}
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": ";
		desc += "When the user is at or below " + getStatValue(PassiveStatName.THRESHOLD) + "% ";
		desc += "of their maximum HP, inflicts them with: ";
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
        b.add("type", "threshold passive");
        return b.build();
    }
    
    public static final ThresholdPassive deserializeJson(JsonObject obj){
        if(!obj.containsKey("name")){
            throw new JsonException("Json Object is missing key 'name'");
        }
        if(!obj.containsKey("stats")){
            throw new JsonException("Json Object is missing key 'stats'");
        }
        JsonArray stats = obj.getJsonArray("stats");
        if(!stats.stream().anyMatch((JsonValue jv) ->{
            return jv.getValueType().equals(JsonValue.ValueType.OBJECT) 
                && ((JsonObject)jv).getString("name").equals("THRESHOLD");
        })){
            throw new JsonException("Json Object is missing key 'THRESHOLD'");
        }
        
        int baseThresh = -1;
        JsonObject temp = null;
        for(JsonValue v : stats){
            if(v.getValueType().equals(JsonValue.ValueType.OBJECT)){
                temp = (JsonObject)v;
                System.out.println(temp);
                if(temp.getString("name").equals("THRESHOLD")){
                    baseThresh = temp.getInt("base");
                }
            }
        }
        ThresholdPassive pass = new ThresholdPassive(getNameFrom(obj), baseThresh);
        
        return pass;
    }
}
