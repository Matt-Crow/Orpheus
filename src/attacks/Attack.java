package attacks;
import java.util.ArrayList;

import entities.Player;
import upgradables.Stat;
import resources.Op;

public class Attack {
	private String name;
	private ArrayList<Stat> stats;
	private int charge;
	private int cooldown;
	
	public Attack(String n, int energyCost, int chargeup, int cooldown, double chargeScale, int range, int offset, int aoe, int areaScale, int distanceScale, int dmg){
		name = n;
		stats = new ArrayList<>();
		stats.add(new Stat("Energy Cost", energyCost, 2));
		stats.add(new Stat("Chargeup", chargeup));
		stats.add(new Stat("Cooldown", cooldown));
		stats.add(new Stat("Charge Scale", chargeScale));
		stats.add(new Stat("Range", range));
		stats.add(new Stat("Offset", offset));
		stats.add(new Stat("AOE", aoe));
		stats.add(new Stat("Area Scale", areaScale));
		stats.add(new Stat("Distance Scale", distanceScale));
		stats.add(new Stat("Damage", dmg));
	}
	public Stat getStat(String n){
		for(Stat stat : stats){
			if(stat.name == n){
				return stat;
			}
		}
		Op.add("The stat by the name of " + n + " is not found for Attack " + name);
		return new Stat("STATNOTFOUND", 0);
	}
	public double getStatValue(String n){
		return getStat(n).get();
	}
	public boolean canUse(Player user){
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
}
