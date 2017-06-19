package attacks;
import java.util.ArrayList;
import java.util.Locale;

import entities.Player;
import upgradables.Stat;

public class Attack {
	private ArrayList<Stat> stats;
	private int charge;
	private int cooldown;
	
	public Attack(int energyCost){
		stats.add(new Stat("Energy Cost", energyCost, 2));
	}
	public void setCUCD(int chargeup, int cooldown){
		stats.add(new Stat("Chargeup", chargeup));
		stats.add(new Stat("Cooldown", cooldown));
	}
	public Stat getStat(String n){
		for(Stat stat : stats){
			if(stat.name.toLowerCase(Locale.ENGLISH) == n.toLowerCase(Locale.ENGLISH)){
				return stat;
			}
		}
		return new Stat("STATNOTFOUND", 0);
	}
	public double getStatValue(String n){
		return getStat(n).get();
	}
	public boolean canUse(Player user){
		return user.getEnergy() >= getStat("EnergyCost").get() && charge >= getStatValue("Chargeup") && cooldown <= 0;
	}
	public void use(Player user){
		if (!canUse(user)){
			return;
		}
	}
	public void init(){
		charge = 0;
		cooldown = 0;
	}
	//double chargeScale, int range, int offset, int aoe, int areaScale, int distanceScale, int dmg
}
