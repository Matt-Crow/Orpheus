package customizables.actives;

import javax.json.*;
import serialization.JsonSerialable;
import statuses.AbstractStatus;
import statuses.StatusTable;
import customizables.CustomizableJsonUtil;

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
    public JsonObject serializeJson(){
        // no new fields are added, so just refer to the superclass' serialization
        return super.serializeJson();
    }
    
    public static BoostActive deserializeJson(JsonObject obj){
        BoostActive ret = new BoostActive(
            CustomizableJsonUtil.getNameFrom(obj),
            CustomizableJsonUtil.getStatusTableFrom(obj)
        );
        getTagsFrom(obj).stream().forEach(t->ret.addTag(t));
        return ret;
    }
	
    @Override
	public BoostActive copy(){
		BoostActive ret = new BoostActive(getName(), getInflict().copy());
        copyTagsTo(ret);
        ret.setColors(getColors());
        return ret;
	}
    @Override
	public void use(){
		super.use();
		StatusTable s = getInflict();
		for(int i = 0; i < s.getSize(); i++){
			getUser().inflict(s.getStatusAt(i));
		}
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "Upon use, inflicts the user with: \n";
		desc += getInflict().getStatusString() + "\n";
		return desc;
	}
}
