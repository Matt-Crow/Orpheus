package attacks;
import java.util.ArrayList;

import entities.*;
import upgradables.Stat;
import resources.Op;

public class Attack {
	private String name;
	private ArrayList<Stat> stats;
	private int cooldown;
	
	public Attack(String n, int energyCost, int cooldown, int range, int speed, int aoe, int areaScale, int distanceScale, int dmg){
		name = n;
		stats = new ArrayList<>();
		stats.add(new Stat("Energy Cost", energyCost, 2));
		stats.add(new Stat("Cooldown", cooldown));
		stats.add(new Stat("Range", range));
		stats.add(new Stat("Speed", speed));
		stats.add(new Stat("AOE", aoe));
		stats.add(new Stat("Area Scale", areaScale));
		stats.add(new Stat("Distance Scale", distanceScale));
		stats.add(new Stat("Damage", dmg));
	}
	public String getName(){
		return name;
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
	public boolean onCooldown(){
		return cooldown > 0;
	}
	public boolean canUse(Player user){
		return user.getEnergy() >= getStat("Energy Cost").get() && !onCooldown();
	}
	public void use(Player user){
		new Projectile(user.getX(), user.getY(), user.getDirNum(), (int) getStatValue("Speed"), user, this);
		
		cooldown = (int) getStatValue("Cooldown");
		displayData();
	}
	public void init(){
		cooldown = 0;
	}
	public void update(){
		cooldown -= 1;
		if (cooldown < 0){
			cooldown = 0;
		}
	}
	public void displayData(){
		for(Stat stat : stats){
			Op.add(stat.name + ": " + getStatValue(stat.name));
		}
		Op.dp();
	}
}
