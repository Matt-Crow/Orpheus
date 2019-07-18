package customizables;

import customizables.characterClass.CharacterClass;
import java.io.File;
import java.util.*;
import javax.json.*;
import serialization.*;
import util.StringUtil;


/**
 * A Build is a collection of 
 * CharacterClass, 
 * 3 active abilities,
 * and 3 passive abilities
 * which are applied to Players at the beginning of a match
 * @author Matt
 */
public class Build implements JsonSerialable{
	private static final HashMap<String, Build> ALL = new HashMap<>();
	private static final Build DEFAULT_EARTH = new Build("Default Earth", "Earth", "Boulder Toss", "Warrior's Stance", "Earthquake", "Toughness", "Determination", "Crippling Hits");
	private static final Build DEFAULT_FIRE = new Build("Default Fire", "Fire", "Mega Firebolt", "Fields of Fire", "Fireball", "Escapist", "Sparking Strikes", "Bracing");
	private static final Build DEFAULT_WATER = new Build("Default Water", "Water", "Waterbolt", "Slash", "Healing Rain", "Toughness", "Bracing", "Recover");
	private static final Build DEFAULT_AIR = new Build("Default Air", "Air", "Mini Windbolt", "Speed Test", "Blade Stance", "Momentum", "Sharpen", "Leechhealer");
	private static final Build TEST = new Build("0x138", "Default", "RAINBOW OF DOOM", "Cursed Daggers", "Speed Test", "Crushing Grip", "Recover", "Cursed");
    
	private final String name;
	private final String className;
	private final String[] activeNames;
	private final String[] passiveNames;
	
	public Build(String buildName, String cName, String a1, String a2, String a3, String p1, String p2, String p3){
		name = buildName;
		className = cName;
		activeNames = new String[]{a1, a2, a3};
		passiveNames = new String[]{p1, p2, p3};
	}
    
    public Build copy(){
        return new Build(
            name,
            className,
            activeNames[0],
            activeNames[1],
            activeNames[2],
            passiveNames[0],
            passiveNames[1],
            passiveNames[2]
        );
    }
    
    @Override
    public JsonObject serializeJson(){
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
    private static String getNameFrom(JsonObject obj){
        JsonUtil.verify(obj, "name");
        return obj.getString("name");
    }
    private static String getCharacterClassFrom(JsonObject obj){
        JsonUtil.verify(obj, "character class");
        return obj.getString("character class");
    }
    private static String[] getActivesFrom(JsonObject obj){
        JsonUtil.verify(obj, "actives");
        JsonArray a = obj.getJsonArray("actives");
        int len = a.size();
        String[] ret = new String[len];
        for(int i = 0; i < len; i++){
            ret[i] = a.getString(i);
        }
        return ret;
    }
    private static String[] getPassivesFrom(JsonObject obj){
        JsonUtil.verify(obj, "passives");
        JsonArray a = obj.getJsonArray("passives");
        int len = a.size();
        String[] ret = new String[len];
        for(int i = 0; i < len; i++){
            ret[i] = a.getString(i);
        }
        return ret;
    }
    public static Build deserializeJson(JsonObject obj){
        String[] actives = getActivesFrom(obj);
        String[] passives = getPassivesFrom(obj);
        return new Build(
            getNameFrom(obj),
            getCharacterClassFrom(obj),
            actives[0],
            actives[1],
            actives[2],
            passives[0],
            passives[1],
            passives[2]
        );
    }
    
    public static void saveAllToFile(File f){
        JsonObject[] objs = ALL.values().stream().map((Build b)->{
            return b.serializeJson();
        }).toArray(size -> new JsonObject[size]);
        JsonUtil.writeToFile(objs, f);  
    }
    
    public static void loadFile(File f){
        Build b = null;
        for(JsonObject obj : JsonUtil.readFromFile(f)){
            b = deserializeJson(obj);
            if(b != null){
                addBuild(b);
            }
            //System.out.println(b.getDescription());
        }
    }
    
    public static final void loadAll(){
        addBuild(DEFAULT_EARTH);
        addBuild(DEFAULT_FIRE);
        addBuild(DEFAULT_WATER);
        addBuild(DEFAULT_AIR);
        addBuild(TEST);
    }
    
    
    
	public static ArrayList<Build> getAllBuilds(){
		return new ArrayList<>(ALL.values());
	}
    public static Build[] getAll(){
        return ALL.values().toArray(new Build[ALL.size()]);
    }
	public static void addBuild(Build b){
		if(b == null){
            throw new NullPointerException();
        }
        ALL.put(b.getName().toLowerCase(), b);
	}
	public static Build getBuildByName(String name){
		if(!ALL.containsKey(name.toLowerCase())){
            throw new NoSuchElementException("No build found with name " + name + ". Did you remember to call Build.addBuild(...)?");
        }
        return ALL.get(name.toLowerCase()).copy();
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
    
    public String getDescription(){
        StringBuilder sb = new StringBuilder();
        sb.append("Build ").append(name).append(": \n");
        sb.append("Class: \n").append(StringUtil.entab(CharacterClass.getCharacterClassByName(className).getDescription())).append("\n");
        sb.append("Actives: \n");
        for(String an : activeNames){
            sb.append("*").append(an).append("\n");
        }
        sb.append("Passives: \n");
        for(String pn : passiveNames){
            sb.append("*").append(pn).append("\n");
        }
        return sb.toString();
    }
}
