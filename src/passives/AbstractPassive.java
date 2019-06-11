package passives;

import serialization.JsonSerialable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import actions.*;
import statuses.StatusTable;
import entities.Player;
import java.io.File;
import java.util.NoSuchElementException;
import javax.json.*;
import serialization.JsonTest;
import statuses.*;
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
	static{
        ThresholdPassive def = new ThresholdPassive("Default", 3);
        def.addStatus(new Resistance(3, 3));
        addPassive(def);
    }
    
	public AbstractPassive(PassiveType t, String n, boolean b){
		super(n);
		type = t;
		targetsUser = b;
	}
    
    public static void loadAll(){
		OnMeleeHitPassive lh = new OnMeleeHitPassive("Leechhealer", true);
		lh.addStatus(new Regeneration(1, 1));
		
		OnMeleeHitPassive m = new OnMeleeHitPassive("Momentum", true);
		m.addStatus(new Rush(1, 1));
		
		OnMeleeHitPassive s = new OnMeleeHitPassive("Sharpen", true);
		s.addStatus(new Strength(1, 1));
		
		OnMeleeHitPassive ss = new OnMeleeHitPassive("Sparking Strikes", true);
		ss.addStatus(new Charge(1, 1));
		
		OnBeHitPassive nh = new OnBeHitPassive("Nature's Healing", true);
		nh.addStatus(new Regeneration(1, 1));
		
		OnBeHitPassive r = new OnBeHitPassive("Recover", true);
		r.addStatus(new Regeneration(2, 1));
		
		OnBeHitPassive t = new OnBeHitPassive("Toughness", true);
		t.addStatus(new Resistance(1, 1));
		
		ThresholdPassive a = new ThresholdPassive("Adrenaline", 3);
		a.addStatus(new Charge(2, 1));
		
		ThresholdPassive b = new ThresholdPassive("Bracing", 3);
		b.addStatus(new Resistance(2, 1));
		
		ThresholdPassive d = new ThresholdPassive("Determination", 3);
		d.addStatus(new Strength(1, 1));
		d.addStatus(new Resistance(1, 1));
		
		ThresholdPassive e = new ThresholdPassive("Escapist", 3);
		e.addStatus(new Rush(2, 1));
		
		ThresholdPassive re = new ThresholdPassive("Retaliation", 3);
		re.addStatus(new Strength(2, 1));
        
        OnHitPassive rc = new OnHitPassive("Recharge", true);
        rc.addStatus(new Charge(1, 1));
        
        OnBeHitPassive cu = new OnBeHitPassive("Cursed", false);
        cu.addStatus(new Stun(3, 3));
		
		addPassives(new AbstractPassive[]{
				lh,
				m,
				s,
				ss,
				nh,
				r,
				t,
				a,
				b,
				d,
				e,
				re,
                rc,
                cu
		});
	}
    public static void saveAll(File f){
        JsonObject[] objs = allPassives.values().stream().map((AbstractPassive p)->{
            return p.serializeJson();
        }).toArray(size -> new JsonObject[size]);
        JsonTest.writeToFile(objs, f);
    }
    
    @Override
	public abstract AbstractPassive copy();
	
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
        if(!allPassives.containsKey(n.toUpperCase())){
            throw new NoSuchElementException("Passive with name " + n + "not found. Did you remember to call AbstractPassive.addPassive(...)?");
        }
		return allPassives.get(n.toUpperCase()).copy();
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
        b.add("type", type.toString());
        b.add("targets user", targetsUser);
        return b.build();
    }
    
    public static AbstractPassive deserializeJson(JsonObject obj){
        if(!obj.containsKey("type")){
            throw new JsonException("JsonObject missing key 'type'");
        }
        AbstractPassive ret = null;
        PassiveType type = PassiveType.fromString(getTypeFrom(obj));
        if(type == null){
            return null; //not a passive
        }
        switch(type){
            case THRESHOLD:
                ret = ThresholdPassive.deserializeJson(obj);
                break;
            case ONMELEEHIT:
                ret = OnMeleeHitPassive.deserializeJson(obj);
                break;
            case ONHIT:
                ret = OnHitPassive.deserializeJson(obj);
                break;
            case ONBEHIT:
                ret = OnBeHitPassive.deserializeJson(obj);
                break;
            default:
                System.out.println("Abstract passive cannot deserialize " + getTypeFrom(obj));
                break;
        }
        return ret;
    }
    
    public static boolean getTargetsUserFrom(JsonObject obj){
        if(!obj.containsKey("targets user")){
            throw new JsonException("JsonObject missing key 'targets user'");
        }
        return obj.getBoolean("targets user");
    }
}
