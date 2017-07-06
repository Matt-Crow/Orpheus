package attacks;
import java.util.ArrayList;
import entities.Player;
import statuses.Status;

public class BoostAttack extends Attack{
	private ArrayList<Status> inflicts;
	public BoostAttack(String n, int energyCost, int cd, Status status){
		super(n, energyCost, cd, 0, 0, 0, 0, 0, 0);
		inflicts = new ArrayList<>();
		inflicts.add(status);
		setType("boost");
	}
	public void addStatus(Status s){
		inflicts.add(s);
	}
	public void use(Player user){
		super.use(user);
		for(Status s : inflicts){
			s.reset();
			user.inflict(s);
		}
	}
}
