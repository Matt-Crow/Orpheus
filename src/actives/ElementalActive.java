package actives;

import serialization.JsonSerialable;
import controllers.Master;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import upgradables.UpgradableJsonUtil;

public class ElementalActive extends AbstractActive implements JsonSerialable{
	public ElementalActive(String n, int arc, int range, int speed, int aoe, int dmg){
		super(ActiveType.ELEMENTAL, n, arc, range, speed, aoe, dmg);
	}
    @Override
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
        copy.setInflict(getInflict());
		return copy;
	}
	
    @Override
	public String getDescription(){
        StringBuilder desc = new StringBuilder();
		desc
            .append(getName())
            .append(": \n");
		if(getStatValue(ActiveStatName.RANGE) == 0){
            desc.append(String.format("The user generates an explosion with a %d unit radius", (int)(getStatValue(ActiveStatName.AOE) / Master.UNITSIZE)));
        } else {
			desc.append("The user launches ");
			if(getStatValue(ActiveStatName.ARC) > 0){
				desc.append(
                    String.format(
                        "projectiles in a %d degree arc, each traveling ", 
                        (int)getStatValue(ActiveStatName.ARC)
                    )
                );
			} else {
				desc.append("a projectile, which travels ");
			}
			desc.append(
                String.format(
                    "for %d units, at %d units per second", 
                    (int)(getStatValue(ActiveStatName.RANGE) / Master.UNITSIZE),
                    (int)(getStatValue(ActiveStatName.SPEED) * Master.FPS / Master.UNITSIZE)
                )
            );
			
			if(getStatValue(ActiveStatName.AOE) != 0){
				desc.append(String.format(" before exploding in a %d unit radius", (int)(getStatValue(ActiveStatName.AOE) / Master.UNITSIZE))); 
			}
		}
        
		desc.append(String.format(" dealing %d damage to enemies it hits. \n", (int)getStatValue(ActiveStatName.DAMAGE)));
		desc.append(String.format("%d energy cost.", getCost()));
		if(getInflict().getSize() > 0){
			desc.append(getInflict().getStatusString());
		}
		
		return desc.toString();
	}

    @Override
    public JsonObject serializeJson(){
        JsonObject obj = ActiveJsonUtil.serializeJson(this);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        b.add("type", "elemental active");
        return b.build();
    }
    
    public static ElementalActive deserializeJson(JsonObject obj){
        ElementalActive ret = new ElementalActive(
            UpgradableJsonUtil.getNameFrom(obj),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.ARC),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.RANGE),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.SPEED),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.AOE),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.DAMAGE)
        );
        ret.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        getTagsFrom(obj).stream().forEach(t->ret.addTag(t));
        ret.setParticleType(getParticleTypeFrom(obj));
        return ret;
    }
}
