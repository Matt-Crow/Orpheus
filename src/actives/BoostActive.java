package actives;
import java.util.ArrayList;
import java.util.HashMap;
import statuses.*;

public class BoostActive extends AbstractActive{
	private ArrayList<Status> inflicts;
	public BoostActive(BoostActiveBlueprint b){
		super(ActiveType.BOOST, b.getName(), b.getCost(), b.getCooldown(), 0, 0, 0, 0);
		inflicts = new ArrayList<>();
		HashMap<StatusName, int[]> statuses = b.getStatuses();
		for(StatusName s : statuses.keySet()){
			//move this elsewhere?
			int i = statuses.get(s)[0];
			int d = statuses.get(s)[1];
			switch(s){
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
	public void use(){
		super.use();
		for(Status s : inflicts){
			s.reset();
			getRegisteredTo().inflict(s);
		}
	}
}
