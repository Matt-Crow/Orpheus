package actives;
import java.util.HashMap;
import statuses.StatusName;

public class BoostActive extends AbstractActive{
	private HashMap<StatusName, Integer[]> inflicts;
	public BoostActive(String n, int cost, int cd, StatusName[] statusNames, int[] intensities, int[] durations){
		super(ActiveType.BOOST, n, cost, cd, 0, 0, 0, 0);
		inflicts = new HashMap<>();
		
		for(int s = 0; s < statusNames.length; s++){
			int i = intensities[s];
			int d = durations[s];
			inflicts.put(statusNames[s], new Integer[]{i, d});
		}
	}
	
	//TODO better way?
	public BoostActive copy(){
		StatusName[] sn = new StatusName[inflicts.size()];
		int[] ints = new int[inflicts.size()];
		int[] durs = new int[inflicts.size()];
		
		for(int i = 0; i < inflicts.size(); i++){
			sn[i] = inflicts.keySet().toArray()[i];
			ints[i] = inflicts.get(i).getIntensityLevel();
			durs[i] = inflicts.get(i).getUses();
		}
		return new BoostActive(getName(), getStatCode()[0], getStatCode()[1], sn, ints, durs);
	}
	public void use(){
		super.use();
		for(Status s : inflicts){
			s.reset();
			getRegisteredTo().inflict(s);
		}
	}
}
