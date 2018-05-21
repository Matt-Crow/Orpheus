package actives;
import java.util.HashMap;
import statuses.StatusName;

public class BoostActiveBlueprint extends AbstractActiveBlueprint{
	/**
	 * Used to prevent passing by referrence
	 */
	private HashMap<StatusName, int[]> statuses;
	
	public BoostActiveBlueprint(String n, int c, int cd, StatusName[] statusNames, int[] intensities, int[] durations){
		super(ActiveType.BOOST, n, c, cd, 0, 0, 0, 0);
		
		statuses = new HashMap<>();
		for(int i = 0; i < statusNames.length && i < intensities.length && i < durations.length; i++){
			statuses.put(statusNames[i], new int[]{intensities[i], durations[i]});
		}
	}
	
	
	public HashMap<StatusName, int[]> getStatuses(){
		return statuses;
	}
}
