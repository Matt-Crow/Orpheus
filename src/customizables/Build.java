package customizables;

import java.util.*;


/**
 * A Build is a collection of 
 * CharacterClass, 
 * 3 active abilities,
 * and 3 passive abilities
 * which are applied to Players at the beginning of a match
 * @author Matt
 */
@SuppressWarnings("unused")
public class Build{
	private static final HashMap<String, Build> ALL = new HashMap<>();
	private static final Build DEFAULT_EARTH = new Build("Default Earth", "Earth", "Boulder Toss", "Warrior's Stance", "Earthquake", "Toughness", "Determination", "Nature's Healing");
	private static final Build DEFAULT_FIRE = new Build("Default Fire", "Fire", "Mega Firebolt", "Fields of Fire", "Fireball", "Escapist", "Sparking Strikes", "Bracing");
	private static final Build DEFAULT_WATER = new Build("Default Water", "Water", "Waterbolt", "Slash", "Healing Rain", "Toughness", "Bracing", "Recover");
	private static final Build DEFAULT_AIR = new Build("Default Air", "Air", "Mini Windbolt", "Speed Test", "Blade Stance", "Momentum", "Sharpen", "Leechhealer");
	private static final Build TEST = new Build("0x138", "Air", "RAINBOW OF DOOM", "Cursed Daggers", "Speed Test", "Momentum", "Recover", "Leechhealer");
    
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
        sb.append(name).append(": \n");
        sb.append("Class: ").append(className).append("\n");
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
