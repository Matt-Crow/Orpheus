package upgradables;

import java.util.ArrayList;
import entities.Player;
import initializers.Master;

public class AbstractUpgradable {
	/**
	 * This class is used as a base 
	 * class for the Active and Passive 
	 * classes, allowing them to be easily
	 * customized by the player in the
	 * customization window
	 */
	private String name;
	private Player registeredTo;
	private ArrayList<Stat> stats;
	private int cooldown;
	
	// constructors
	public AbstractUpgradable(String n){
		name = n;
		stats = new ArrayList<>();
		cooldown = 0;
	}
	
	// setters and getters
	public String getName(){
		return name;
	}
	@Override
	public String toString(){
		return name;
	}
	public String getDescription(){
		return getName() + " does not have a description";
	}
	public void registerTo(Player p){
		registeredTo = p;
	}
	public Player getRegisteredTo(){
		return registeredTo;
	}
	// change this to replace old?
	public void addStat(Stat s){
		boolean exists = false;
		for(Stat existingStat : stats){
			if(existingStat.getName() == s.getName()){
				exists = true;
			}
		}
		if(!exists){
			stats.add(s);
		}
	}
	public void addStat(String n, double base){
		addStat(new Stat(n, base));
	}
	public void addStat(String n, double base, double maxRelativeToMin){
		// maxRelativeToMin is how much the stat's value is at max.
		// EX: base of 12.5, maxRelativeToMin of 2.0 will result it 25.0 max
		addStat(new Stat(n, base, maxRelativeToMin));
	}
	public Stat getStat(String n){
		Stat ret = new Stat("STATNOTFOUND", 0);
		n = n.toUpperCase();
		for(Stat stat : stats){
			if(stat.getName().toUpperCase().equals(n)){
				ret = stat;
			}
		}
		if(ret.getName() == "STATNOTFOUND"){
			throw new NullPointerException();
		}
		return ret;
	}
	public double getStatValue(String n){
		return getStat(n).get();
	}
	public void setCooldown(int seconds){
		addStat("Cooldown", Master.seconds(seconds));
	}
	public int getCooldown(){
		return cooldown;
	}
	public void setToCooldown(){
		cooldown = (int) getStatValue("Cooldown");
	}
	public boolean onCooldown(){
		return cooldown > 0;
	}
	
	// in battle functions
	public void init(){
		cooldown = 0;
	}
	public void update(){
		cooldown -= 1;
		if (cooldown < 0){
			cooldown = 0;
		}
	}
}
