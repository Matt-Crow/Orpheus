package customizables;

import PsuedoJson.JsonSerialable;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


@SuppressWarnings("unused")
public class Build implements JsonSerialable{
	private static final ArrayList<Build> builds = new ArrayList<>();
	private static final Build defaultEarth = new Build("Default Earth", "Earth", "Boulder Toss", "Warrior's Stance", "Earthquake", "Toughness", "Determination", "Nature's Healing");
	private static final Build defaultFire = new Build("Default Fire", "Fire", "Mega Firebolt", "Fields of Fire", "Fireball", "Escapist", "Sparking Strikes", "Bracing");
	private static final Build defaultWater = new Build("Default Water", "Water", "Waterbolt", "Slash", "Healing Rain", "Toughness", "Bracing", "Recover");
	private static final Build defaultAir = new Build("Default Air", "Air", "Mini Windbolt", "Speed Test", "Blade Stance", "Momentum", "Sharpen", "Leechhealer");
	private static final Build test = new Build("0x138", "Air", "RAINBOW OF DOOM", "Cursed Daggers", "Speed Test", "Momentum", "Recover", "Leechhealer");
	
	private final String name;
	private final String className;
	private final String[] activeNames;
	private final String[] passiveNames;
	
	public Build(String buildName, String cName, String a1, String a2, String a3, String p1, String p2, String p3){
		name = buildName;
		className = cName;
		activeNames = new String[]{a1, a2, a3};
		passiveNames = new String[]{p1, p2, p3};
		addBuild(this);
	}
	public static ArrayList<Build> getAllBuilds(){
		return builds;
	}
	public static void addBuild(Build b){
		int removeIndex = -1;
		for(int i = 0; i < builds.size(); i++){
			if(builds.get(i).getName().equals(b.getName())){
				removeIndex = i;
			}
		}
		if(removeIndex != -1){
			builds.remove(removeIndex);
		}
		builds.add(b);
	}
	public static Build getBuildByName(String name){
		for(Build b : builds){
			if(b.name == null ? name == null : b.name.equals(name)){
				return b;
			}
		}
		return defaultEarth;
	}
	public String getName(){
		return name;
	}
	public String getClassName(){
		return className;
	}
	public String[] getActiveNames(){
		return activeNames;
	}
	public String[] getPassiveNames(){
		return passiveNames;
	}

    @Override
    public JsonObject serializeJson() {
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("type", "build");
        b.add("name", name);
        b.add("character class", className);
        
        JsonArrayBuilder acts = Json.createArrayBuilder();
        for(String activeName : activeNames){
            acts.add(activeName);
        }
        b.add("actives", acts.build());
        
        JsonArrayBuilder pass = Json.createArrayBuilder();
        for(String passName : passiveNames){
            pass.add(passName);
        }
        b.add("passives", pass.build());
        
        return b.build();
    }
}
