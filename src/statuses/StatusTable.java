package statuses;

import PsuedoJson.JsonSerialable;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

// redo this to be less aweful
public class StatusTable implements JsonSerialable{
	/**
	 * Used to store status data for actives and passives
	 */
	private ArrayList<StatusName> names;
	private ArrayList<Integer> intensities;
	private ArrayList<Integer> durations;
	private ArrayList<Integer> chances;
	
	public StatusTable(){
		names = new ArrayList<>();
		intensities = new ArrayList<>();
		durations = new ArrayList<>();
		chances = new ArrayList<>();
	}
	public StatusTable(StatusName[] ns, int[] is, int[] ds, int[] cs){
		this();
		for(StatusName n : ns){
			names.add(n);
		}
		for(int i : is){
			intensities.add(i);
		}
		for(int d : ds){
			durations.add(d);
		}
		for(int c : cs){
			chances.add(c);
		}
	}
	public StatusTable(StatusName[] ns, int[] is, int[] ds){
		this();
		for(int i = 0; i < ns.length; i++){
			add(ns[i], is[i], ds[i], 100);
		}
		
	}
	public StatusTable copy(){
		StatusTable ret = new StatusTable();
		for(int i = 0; i < names.size(); i++){
			ret.add(names.get(i), intensities.get(i).intValue(), durations.get(i).intValue(), chances.get(i).intValue());
		}
		return ret;
	}
	public void add(StatusName n, int i, int d, int c){
		names.add(n);
		intensities.add(i);
		durations.add(d);
		chances.add(c);
	}
	public void add(StatusName n, int i, int d){
		add(n, i, d, 100);
	}
	
	public StatusName getNameAt(int i){
		return names.get(i);
	}
	public int getIntensityAt(int i){
		return intensities.get(i);
	}
	public int getDurationAt(int i){
		return durations.get(i);
	}
	public int getChanceAt(int i){
		return chances.get(i);
	}
	public AbstractStatus getStatusAt(int i){
		return AbstractStatus.decode(names.get(i), intensities.get(i), durations.get(i));
	}
	public int getSize(){
		return names.size();
	}
	
	public String getStatusString(){
		String desc = "~~~STATUS DATA~~~";
		for(int i = 0; i < getSize(); i++){
			if(getChanceAt(i) < 100){
				desc += "\n" + getChanceAt(i) + "% chance to inflict";
			}
			desc += "\n" + AbstractStatus.decode(getNameAt(i), getIntensityAt(i), getDurationAt(i)).getDesc();
		}
		return desc;
	}

    @Override
    public JsonObject serializeJson() {
        JsonObjectBuilder obj = Json.createObjectBuilder();
        obj.add("type", "status table");
        JsonArrayBuilder statuses = Json.createArrayBuilder();
        for(int i = 0; i < getSize(); i++){
            statuses.add(AbstractStatus.decode(getNameAt(i), getIntensityAt(i), getDurationAt(i)).serializeJson());
        }
        return obj.build();
    }
    
    public static Object deserializeJson(JsonObject obj){
        return obj;
    }
}
