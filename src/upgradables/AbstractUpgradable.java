package upgradables;

import java.util.Collection;
import java.util.HashMap;
import entities.Player;
import initializers.Master;
import statuses.StatusName;
import statuses.StatusTable;

// TODO: Make characterClass extend from this?
public abstract class AbstractUpgradable {
	/**
	 * This class is used as a base 
	 * class for the Active and Passive 
	 * classes, allowing them to be easily
	 * customized by the player in the
	 * customization window
	 */
	private String name; // would like to use enum, but looks like I can't
	private Player registeredTo;
	private HashMap<String, Stat> stats;
	private HashMap<String, Integer> bases;
	private int cooldown; // frames until this upgradable can be used in battle again
	private StatusTable inflict; // statuses that this may inflict. Each subclass handles this themself
	
	// constructors
	public AbstractUpgradable(String n){
		name = n;
		stats = new HashMap<>();
		bases = new HashMap<>();
		inflict = new StatusTable();
		cooldown = 0;
	}
	public AbstractUpgradable copy(){
		return this;
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
	
	// stat stuff
	public void addStat(Stat s){
		stats.put(s.getName().toUpperCase(), s);
	}
	public void addStat(String n, double base){
		addStat(new Stat(n.toUpperCase(), base));
	}
	public void addStat(String n, double base, double maxRelativeToMin){
		// maxRelativeToMin is how much the stat's value is at max.
		// EX: base of 12.5, maxRelativeToMin of 2.0 will result it 25.0 max
		addStat(new Stat(n.toUpperCase(), base, maxRelativeToMin));
	}
	public Stat getStat(String n){
		Stat ret = new Stat("STATNOTFOUND", 0);
		n = n.toUpperCase();
		if(!stats.containsKey(n)){
			throw new NullPointerException("Stat not found for " + registeredTo.getName() + " with name " + n);
		} else {
			ret = stats.get(n);
		}
		return ret;
	}
	
	public double getStatValue(String n){
		return getStat(n).get();
	}
	public HashMap<String, Stat> getStats(){
		return stats;
	}
	
	// base stuff
	public void setBase(String statName, int value){
		bases.put(statName.toUpperCase(), value);
	}
	public int getBase(String statName){
		return bases.get(statName.toUpperCase());
	}
	public int[] getAllBaseValues(){
		int[] ret = new int[bases.size()];
		Collection<Integer> values = bases.values();
		int i = 0;
		for(Integer value : values){
			ret[i] = value;
			i++;
		}
		return ret;
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
	
	// status methods
	public void addStatus(StatusName n, int intensity, int duration, int chance){
		inflict.add(n, intensity, duration, chance);
	}
	public void addStatus(StatusName n, int intensity, int duration){
		addStatus(n, intensity, duration, 100);
	}
	public void setInflict(StatusTable s){
		inflict = s.copy();
	}
	public StatusTable getInflict(){
		return inflict;
	}
	public void copyInflictTo(AbstractUpgradable a){
		/* takes all the statuses from this upgradable's
		 * status table, and copies them to p's
		 */
		for(int i = 0; i < inflict.getSize(); i++){
			a.addStatus(inflict.getNameAt(i), inflict.getIntensityAt(i), inflict.getDurationAt(i), inflict.getChanceAt(i));
		}
	}
	
	// in battle functions
	public void init(){
		cooldown = 0;
		for(String s : stats.keySet()){
			stats.get(s).calc();
		}
	}
	
	public void update(){
		cooldown -= 1;
		if (cooldown < 0){
			cooldown = 0;
		}
	}
}
