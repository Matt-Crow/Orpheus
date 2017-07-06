package customizables;

import java.util.ArrayList;

public class Build {
	private static ArrayList<Build> builds = new ArrayList<>();
	private static Build defaultEarth = new Build("Default Earth", "Earth", "Rupture", "Warrior's Stance", "");
	private static Build defaultFire = new Build("Default Fire", "Fire", "Fireball", "Fields of Fire", "Flamethrower");
	private static Build defaultWater = new Build("Default Water", "Water", "Shield Stance", "", "");
	private static Build defaultAir = new Build("Default Air", "Air", "Blade Stance", "", "");
	
	private String name;
	private String className;
	private String[] activeNames;
	
	public Build(String buildName, String cName, String a1, String a2, String a3){
		name = buildName;
		className = cName;
		activeNames = new String[]{a1, a2, a3};
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
}
