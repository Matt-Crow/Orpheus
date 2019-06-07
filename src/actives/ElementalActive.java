package actives;

import PsuedoJson.JsonSerialable;
import PsuedoJson.PsuedoJsonObject;
import controllers.Master;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class ElementalActive extends AbstractActive implements JsonSerialable{
	public ElementalActive(String n, int arc, int range, int speed, int aoe, int dmg){
		super(ActiveType.ELEMENTAL, n, arc, range, speed, aoe, dmg);
	}
	public ElementalActive copy(){
		ElementalActive copy = new ElementalActive(
				getName(), 
				getBase(ActiveStatName.ARC), 
				getBase(ActiveStatName.RANGE), 
				getBase(ActiveStatName.SPEED), 
				getBase(ActiveStatName.AOE), 
				getBase(ActiveStatName.DAMAGE));
		copy.setParticleType(getParticleType());
		copyTagsTo(copy);
		return copy;
	}
	
	public void use(){
		super.use();
	}
	
	public String getDescription(){
		String desc = getName() + ": \n";
		if(getStatValue(ActiveStatName.RANGE) != 0){
			desc += "The user launches ";
			if(getStatValue(ActiveStatName.ARC) > 0){
				desc += "projectiles in a " + (int)getStatValue(ActiveStatName.ARC) + " degree arc, each traveling ";
			} else {
				desc += "a projectile, which travels ";
			}
			desc += "for " + (int)(getStatValue(ActiveStatName.RANGE) / Master.UNITSIZE) + " units, at " + (int)(getStatValue(ActiveStatName.SPEED) * Master.FPS / Master.UNITSIZE) + " units per second";
			
			if(getStatValue(ActiveStatName.AOE) != 0){
				desc += " before exploding in a " + (int)(getStatValue(ActiveStatName.AOE) / Master.UNITSIZE)+ " unit radius,"; 
			}
		} else {
			desc += "The user generates an explosion ";
			desc += "with a " + (int)(getStatValue(ActiveStatName.AOE) / Master.UNITSIZE) + " unit radius,";
		}
		desc += " dealing " + (int)getStatValue(ActiveStatName.DAMAGE) + " damage to enemies it hits. \n";
		desc += getCost() + " energy cost.";
		if(getInflict().getSize() > 0){
			desc += getInflict().getStatusString();
		}
		
		return desc;
	}

    @Override
    public PsuedoJsonObject getPsuedoJson() {
        PsuedoJsonObject j = new PsuedoJsonObject(getName());
        
        ActiveStatName[] keys = new ActiveStatName[]{
            ActiveStatName.AOE,
            ActiveStatName.ARC,
            ActiveStatName.DAMAGE,
            ActiveStatName.RANGE,
            ActiveStatName.SPEED
        };
        j.addPair("Type", this.getType().toString());
        for(ActiveStatName n : keys){
            j.addPair(n.toString(), getBase(n) + "");
        }
        j.addPair(ActiveStatName.PARTICLETYPE.toString(), getParticleType().toString());
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
        b.add("type", "elemental active");
        return b.build();
    }
    
}
