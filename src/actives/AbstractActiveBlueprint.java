package actives;

import java.util.ArrayList;

public abstract class AbstractActiveBlueprint {
	/**
	 * Blueprints are used as parameters when constructing actives
	 * 
	 * 
	 * oh wait, just do copy constructor...
	 */
	private String activeName;
	private int energyCost;
	private int cooldown;
	private int range;
	private int speed;
	private int aoe;
	private int dmg;
	
	private ActiveType type;
	
	private static ArrayList<AbstractActiveBlueprint> allBlueprints = new ArrayList<>();
	
	public AbstractActiveBlueprint(ActiveType t, String n, int c, int cd, int r, int s, int area, int damage){
		type = t;
		activeName = n;
		energyCost = c;
		cooldown = cd;
		range = r;
		speed = s;
		aoe = area;
		dmg = damage;
		allBlueprints.add(this);
	}
	public ActiveType getType(){
		return type;
	}
	
	public String getName(){
		return activeName;
	}
	public int getCost(){
		return energyCost;
	}
	public int getCooldown(){
		return cooldown;
	}
	public int getRange(){
		return range;
	}
	public int getSpeed(){
		return speed;
	}
	public int getAoe(){
		return aoe;
	}
	public int getDmg(){
		return dmg;
	}
	public static AbstractActiveBlueprint getBlueprintByName(String n){
		AbstractActiveBlueprint ret = allBlueprints.get(0);
		boolean found = false;
		for(int i = 0; i < allBlueprints.size() && !found; i++){
			if(n.toUpperCase().equals(allBlueprints.get(i).getName().toUpperCase())){
				ret = allBlueprints.get(i);
				found = true;
			}
		}
		if(!found){
			throw new NullPointerException();
		}
		return ret;
	}
}
