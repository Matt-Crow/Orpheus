package attacks;
import java.util.ArrayList;

import entities.Player;
import upgradables.Stat;

import resources.Op;

public class Attack {
	private ArrayList<Stat> stats;
	private int charge;
	private int cooldown;
	
	public Attack(int energyCost){
		stats = new ArrayList<>();
		stats.add(new Stat("Energy Cost", energyCost, 2));
	}
	public void setCUCD(int chargeup, int cooldown){
		stats.add(new Stat("Chargeup", chargeup));
		stats.add(new Stat("Cooldown", cooldown));
	}
	public Stat getStat(String n){
		for(Stat stat : stats){
			if(stat.name == n){
				return stat;
			}
		}
		return new Stat("STATNOTFOUND", 0);
	}
	public double getStatValue(String n){
		return getStat(n).get();
	}
	public boolean canUse(Player user){
		Op.add(getStatValue("Cooldown") + " CD");
		Op.dp();
		return user.getEnergy() >= getStat("Energy Cost").get() && charge >= getStatValue("Chargeup") && cooldown <= 0;
	}
	public void use(Player user){
		charge += 1;
		if (!canUse(user)){
			return;
		}
		// do effect
		charge = 0;
		cooldown = (int) getStatValue("Cooldown");
	}
	public void init(){
		charge = 0;
		cooldown = 0;
	}
	public void update(){
		cooldown -= 1;
		if (cooldown < 0){
			cooldown = 0;
		}
	}
	//double chargeScale, int range, int offset, int aoe, int areaScale, int distanceScale, int dmg
}
