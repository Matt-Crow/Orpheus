package customizables;

import java.util.ArrayList;

public class Build {
	private static ArrayList<Build> builds = new ArrayList<>();
	private static Build defaultEarth = new Build("Default Earth", "Earth", "Boulder Toss", "Warrior's Stance", "Earthquake", "Toughness", "Determination", "Nature's Healing");
	private static Build defaultFire = new Build("Default Fire", "Fire", "Fireball", "Fields of Fire", "Burning Rage", "Retaliation", "Escapist", "");
	private static Build defaultWater = new Build("Default Water", "Water", "Shield Stance", "", "", "Bracing", "Revitalize", "");
	private static Build defaultAir = new Build("Default Air", "Air", "Blade Stance", "", "", "Retaliation", "Adrenaline", "");
	
	private String name;
	private String className;
	private String[] activeNames;
	private String[] passiveNames;
	
	public Build(String buildName, String cName, String a1, String a2, String a3, String p1, String p2, String p3){
		name = buildName;
		className = cName;
		activeNames = new String[]{a1, a2, a3};
		passiveNames = new String[]{p1, p2, p3};
		builds.add(this);
	}
	public static Build getBuildByName(String name){
		for(Build b : builds){
			if(b.name == name){
				return b;
			}
		}
		return defaultEarth;
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
