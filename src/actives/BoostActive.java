package actives;
import java.util.ArrayList;
import statuses.*;

public class BoostActive extends AbstractActive{
	private ArrayList<Status> inflicts;
	public BoostActive(String n, int cost, int cd, StatusName[] statusNames, int[] intensities, int[] durations){
		super(ActiveType.BOOST, n, cost, cd, 0, 0, 0, 0);
		inflicts = new ArrayList<>();
		for(int s = 0; s < statusNames.length; s++){
			//move this elsewhere?
			int i = intensities[s];
			int d = durations[s];
			switch(statusNames[s]){
			case BURN:
				inflicts.add(new Burn(i, d));
				break;
			case CHARGE:
				inflicts.add(new Charge(i, d));
				break;
			case HEALING:
				inflicts.add(new Healing(i));
				break;
			case REGENERATION:
				inflicts.add(new Regeneration(i, d));
				break;
			case RESISTANCE:
				inflicts.add(new Resistance(i, d));
				break;
			case RUSH:
				inflicts.add(new Rush(i, d));
				break;
			case STRENGTH:
				inflicts.add(new Strength(i, d));
				break;
			case STUN:
				inflicts.add(new Stun(i, d));
				break;
			}
		}
	}
	
	//TODO better way?
	public BoostActive copy(){
		StatusName[] sn = new StatusName[inflicts.size()];
		int[] ints = new int[inflicts.size()];
		int[] durs = new int[inflicts.size()];
		
		for(int i = 0; i < inflicts.size(); i++){
			switch(inflicts.get(i).getName().toUpperCase()){
			case "BURN":
				sn[i] = StatusName.BURN;
				break;
			case "CHARGE":
				sn[i] = StatusName.CHARGE;
				break;
			case "HEALING":
				sn[i] = StatusName.HEALING;
				break;
			case "REGENERATION":
				sn[i] = StatusName.REGENERATION;
				break;
			case "RESISTANCE":
				sn[i] = StatusName.RESISTANCE;
				break;
			case "RUSH":
				sn[i] = StatusName.RUSH;
				break;
			case "STRENGTH":
				sn[i] = StatusName.STRENGTH;
				break;
			case "STUN":
				sn[i] = StatusName.STUN;
				break;
			default:
				throw new NullPointerException();
			}
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
