package actives;
import serialization.JsonSerialable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import statuses.AbstractStatus;
import statuses.StatusTable;
import upgradables.UpgradableJsonUtil;

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
	
    @Override
	public BoostActive copy(){
		BoostActive ret = new BoostActive(getName(), getInflict().copy());
        copyTagsTo(ret);
        return ret;
	}
    @Override
	public void use(){
		super.use();
		StatusTable s = getInflict();
		for(int i = 0; i < s.getSize(); i++){
			getRegisteredTo().inflict(s.getStatusAt(i));
		}
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "Upon use, inflicts the user with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
    
    @Override
    public JsonObject serializeJson(){
        JsonObject obj = ActiveJsonUtil.serializeJson(this);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        b.add("type", "boost active");
        return b.build();
    }
    
    public static BoostActive deserializeJson(JsonObject obj){
        BoostActive ret = new BoostActive(
            UpgradableJsonUtil.getNameFrom(obj),
            UpgradableJsonUtil.getStatusTableFrom(obj)
        );
        getTagsFrom(obj).stream().forEach(t->ret.addTag(t));
        return ret;
    }
}
