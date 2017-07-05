package attacks;
import entities.Player;
import statuses.Status;

public class BoostAttack extends Attack{
	private Status inflicts;
	public BoostAttack(String n, int energyCost, int cd, Status status){
		super(n, energyCost, cd, 0, 0, 0, 0, 0, 0);
		inflicts = status;
	}
	public void use(Player user){
		super.use(user);
		inflicts.reset();
		user.inflict(inflicts);
	}
}
