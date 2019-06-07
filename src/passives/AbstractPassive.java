package passives;

import PsuedoJson.JsonSerialable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import actions.*;
import statuses.StatusTable;
import entities.Player;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import util.Op;
import util.Random;
import upgradables.AbstractUpgradable;

public abstract class AbstractPassive extends AbstractUpgradable<PassiveStatName> implements JsonSerialable{
	/**
	 * Passives are abilities that have specific triggers, 
	 * i.e., the user does not directly trigger them:
	 * they are triggered passively
	 */
	private PassiveType type; // used when upcasting
	private boolean targetsUser;
	
	private static HashMap<String, AbstractPassive> allPassives = new HashMap<>();
	
	public AbstractPassive(PassiveType t, String n, boolean b){
		super(n);
		type = t;
		targetsUser = b;
	}
	public AbstractPassive copy(){
		// DO NOT INVOKE THIS
		return this;
	}
	
	// static methods
	public static void addPassive(AbstractPassive p){
		allPassives.put(p.getName().toUpperCase(), p);
	}
	public static void addPassives(AbstractPassive[] ps){
		for(AbstractPassive p : ps){
			addPassive(p);
		}
	}
	public static AbstractPassive getPassiveByName(String n){
		AbstractPassive ret = allPassives.getOrDefault(n.toUpperCase(), allPassives.get("SLASH"));
		if(ret.getName().toUpperCase().equals("SLASH") && !n.toUpperCase().equals("SLASH")){
			Op.add("No passive was found with name " + n + " in AbstractPassive.getPassiveByName");
			Op.dp();
		}
		return ret;
	}
	public static AbstractPassive[] getAll(){
		AbstractPassive[] ret = new AbstractPassive[allPassives.size()];
		Collection<AbstractPassive> values = allPassives.values();
		int i = 0;
		for(AbstractPassive ap : values){
			ret[i] = ap;
			i++;
		}
		return ret;
	}
	public static String[] getAllNames(){
		String[] ret = new String[allPassives.size()];
		Set<String> keys = allPassives.keySet();
		int i = 0;
		for(String key : keys){
			ret[i] = key;
			i++;
		}
		return ret;
	}
	
	public void setStat(PassiveStatName n, int value){
		Op.add("Method addStat is not defined for class " + getClass().getName());
		Op.dp();
	}
	
	// setters / getters
	public PassiveType getType(){
		return type;
	}
	public boolean getTargetsUser(){
		return targetsUser;
	}
	
	// in battle methods. These are applied in the subclasses
	public void applyEffect(Player p){
		StatusTable inf = getInflict();
		for(int i = 0; i < inf.getSize(); i++){
			p.inflict(inf.getStatusAt(i));
		}
	}
	public OnHitListener getKey(){
		OnHitListener a = new OnHitListener(){
            @Override
			public void actionPerformed(OnHitEvent t){
				StatusTable inf = getInflict();
				for(int i = 0; i < inf.getSize(); i++){
                    if(getTargetsUser()){
                        getRegisteredTo().inflict(inf.getStatusAt(i));
                    } else {
                        ((Player)t.getWasHit()).inflict(inf.getStatusAt(i));
                    }
				}
			}
		};
		return a;
	}
    @Override
	public void update(){
		
	}
    
    @Override
    public abstract String getDescription();
    
    @Override
    public JsonObject serializeJson(){
        JsonObject obj = super.serializeJson();
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        b.add("type", "passive");
        b.add("targets user", targetsUser);
        return b.build();
    }
    
    public static AbstractPassive deserializeJson(JsonObject obj){
        if(!obj.containsKey("type")){
            throw new JsonException("JsonObject missing key 'type'");
        }
        AbstractPassive ret = null;
        switch(getTypeFrom(obj)){
            case "threshold passive":
                ret = ThresholdPassive.deserializeJson(obj);
                break;
            default:
                System.out.println("Cannot deserialize " + getTypeFrom(obj));
                break;
        }
        return ret;
    }
}
