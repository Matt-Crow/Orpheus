package actives;
import java.util.HashMap;
import java.util.ArrayList;
import statuses.StatusName;

public class BoostActiveBlueprint {
	/**
	 * Used to prevent passing by referrence
	 */
	private String name;
	private int cost;
	private int cooldown;
	private HashMap<StatusName, int[]> statuses;
	
	private static ArrayList<BoostActiveBlueprint> allBlueprints = new ArrayList<>(); // move this to superclass
	
	public BoostActiveBlueprint(String n, int c, int cd, StatusName[] statusNames, int[] intensities, int[] durations){
		name = n;
		cost = c;
		cooldown = cd;
		statuses = new HashMap<>();
		for(int i = 0; i < statusNames.length && i < intensities.length && i < durations.length; i++){
			statuses.put(statusNames[i], new int[]{intensities[i], durations[i]});
		}
		allBlueprints.add(this);
	}
	
	public String getName(){
		return name;
	}
	public int getCost(){
		return cost;
	}
	public int getCooldown(){
		return cooldown;
	}
	public HashMap<StatusName, int[]> getStatuses(){
		return statuses;
	}
	public BoostActiveBlueprint getBlueprintByName(String n){
		BoostActiveBlueprint ret = allBlueprints.get(0);
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
