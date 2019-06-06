package actives;
import PsuedoJson.JsonSerialable;
import PsuedoJson.PsuedoJsonObject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import statuses.AbstractStatus;
import statuses.StatusTable;

public class BoostActive extends AbstractActive implements JsonSerialable{
    public BoostActive(String n, StatusTable t){
        super(ActiveType.BOOST, n, 0, 0, 0, 0, 0);
        setInflict(t);
    }
    public BoostActive(String n, AbstractStatus[] st){
        super(ActiveType.BOOST, n, 0, 0, 0, 0, 0);
        for(AbstractStatus s : st){
            addStatus(s);
        }
    }
	
	public BoostActive copy(){
		BoostActive ret = new BoostActive(getName(), getInflict().copy());
        copyTagsTo(ret);
        return ret;
	}
	public void use(){
		super.use();
		StatusTable s = getInflict();
		for(int i = 0; i < s.getSize(); i++){
			getRegisteredTo().inflict(s.getStatusAt(i));
		}
	}
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "Upon use, inflicts the user with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}

    @Override
    public PsuedoJsonObject getPsuedoJson() {
        PsuedoJsonObject j = new PsuedoJsonObject(getName());
        
        j.addPair("Type", this.getType().toString());
        
        
        StatusTable s = getInflict();
        PsuedoJsonObject status = null;
        AbstractStatus st;
        for(int i = 0; i < s.getSize(); i++){
            st = s.getStatusAt(i);
            status = new PsuedoJsonObject(st.getName());
            status.addPair("Intensity", st.getIntensityLevel() + "");
            status.addPair("Duration", st.getBaseUses() + "");
            j.addPair(st.getName(), status);
        }
        j.addPair("Tags", getTagPsuedoJson());
        
        return j;
    }
    
    @Override
    public JsonObject serializeJson(){
        JsonObject obj = super.serializeJson();
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        b.add("type", "boost active");
        return b.build();
    }
    
    public static Object deserializeJson(JsonObject obj){
        return obj;
    }
}
