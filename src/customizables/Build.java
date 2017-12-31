package customizables;

import java.util.ArrayList;

import resources.Op;

@SuppressWarnings("unused")
public class Build {
	private static ArrayList<Build> builds = new ArrayList<>();
	private static Build defaultEarth = new Build("Default Earth", "Earth", "Boulder Toss", "Warrior's Stance", "Earthquake", "Toughness", "Determination", "Nature's Healing");
	private static Build defaultFire = new Build("Default Fire", "Fire", "Mega Firebolt", "Fields of Fire", "Burning Rage", "Escapist", "Sparking Strikes", "Bracing");
	private static Build defaultWater = new Build("Default Water", "Water", "Waterbolt", "Heal", "Healing Rain", "Toughness", "Revitalize", "Recover");
	private static Build defaultAir = new Build("Default Air", "Air", "Mini Windbolt", "Flurry", "Blade Stance", "Momentum", "Sharpen", "Leechhealer");
	private static Build test = new Build("0x138", "Earth", "RAINBOW OF DOOM", "RAINBOW OF DOOM", "Tracking Projectile Test", "Momentum", "Recover", "Leechhealer");
	
	private String name;
	private String className;
	private String[] activeNames;
	private String[] passiveNames;
	
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
			if(b.name == name){
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
}
