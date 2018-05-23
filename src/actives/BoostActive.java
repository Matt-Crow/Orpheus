package actives;
import statuses.StatusName;
import statuses.StatusTable;

public class BoostActive extends AbstractActive{
	public BoostActive(String n, int cost, int cd, StatusName[] statusNames, int[] intensities, int[] durations){
		super(ActiveType.BOOST, n, cost, cd, 0, 0, 0, 0);
		
		for(int s = 0; s < statusNames.length; s++){
			addStatus(statusNames[s], intensities[s], durations[s], 100);
		}
	}
	
	public BoostActive copy(){
		StatusTable orig = getInflict();
		StatusName[] sn = new StatusName[orig.getSize()];
		int[] ints = new int[orig.getSize()];
		int[] durs = new int[orig.getSize()];
		
		for(int i = 0; i < orig.getSize(); i++){
			sn[i] = orig.getNameAt(i);
			ints[i] = orig.getIntensityAt(i);
			durs[i] = orig.getDurationAt(i);
		}
		return new BoostActive(getName(), getBases().getCost(), getBases().getCooldown(), sn, ints, durs);
	}
	public void use(){
		super.use();
		StatusTable s = getInflict();
		for(int i = 0; i < s.getSize(); i++){
			getRegisteredTo().inflict(s.getNameAt(i), s.getIntensityAt(i), s.getDurationAt(i));
		}
	}
}
