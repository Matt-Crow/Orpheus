package attacks;
import java.util.ArrayList;
import statuses.Status;

public class BoostActive extends Active{
	private ArrayList<Status> inflicts;
	public BoostActive(String n, int energyCost, int cd, Status status){
		super(n, energyCost, cd, 0, 0, 0, 0);
		inflicts = new ArrayList<>();
		inflicts.add(status);
	}
	public void addStatus(Status s){
		inflicts.add(s);
	}
	public void use(){
		super.use();
		for(Status s : inflicts){
			s.reset();
			getRegisteredTo().inflict(s);
		}
	}
}
