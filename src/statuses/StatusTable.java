package statuses;

import PsuedoJson.JsonSerialable;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

// redo this to be less aweful
public class StatusTable implements JsonSerialable{
	/**
	 * Used to store status data for actives and passives
	 */
    
	private final ArrayList<AbstractStatus> statuses;
    
	public StatusTable(){
        statuses = new ArrayList<>();
	}
	public StatusTable copy(){
		StatusTable ret = new StatusTable();
        statuses.forEach((AbstractStatus s) -> ret.add(s));
		return ret;
	}
	public void add(AbstractStatus s){
		statuses.add(s.copy());
	}
    
    /**
     * 
     * @param i
     * @return a copy of the status at index i
     */
	public AbstractStatus getStatusAt(int i){
		return statuses.get(i).copy();
	}
	public int getSize(){
		return statuses.size();
	}
	
	public String getStatusString(){
		String desc = "~~~STATUS DATA~~~";
		for(int i = 0; i < getSize(); i++){
			desc += "\n" + statuses.get(i).getDesc();
		}
		return desc;
	}

    @Override
    public JsonObject serializeJson() {
        JsonObjectBuilder obj = Json.createObjectBuilder();
        obj.add("type", "status table");
        JsonArrayBuilder statusJson = Json.createArrayBuilder();
        for(int i = 0; i < getSize(); i++){
            statusJson.add(statuses.get(i).serializeJson());
        }
        obj.add("statuses", statusJson.build());
        return obj.build();
    }
    
    public static StatusTable deserializeJson(JsonObject obj){
        StatusTable st = new StatusTable();
        if(!obj.containsKey("statuses")){
            throw new JsonException("Json Object is missing key 'statuses'");
        }
        JsonArray a = obj.getJsonArray("statuses");
        a
            .stream()
            .filter((JsonValue v)->v.getValueType().equals(JsonValue.ValueType.OBJECT))
            .forEach((JsonValue val)->{
                st.add(AbstractStatus.deserializeJson((JsonObject)val));
            });
        return st;
    }
}
