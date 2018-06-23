package upgradables;

import java.util.Collection;
import java.util.HashMap;
import entities.Player;
import initializers.Master;
import statuses.StatusName;
import statuses.StatusTable;

//T is an enum
public abstract class AbstractUpgradable<T> {
	/**
	 * This class is used as a base 
	 * class for the Active and Passive 
	 * classes, allowing them to be easily
	 * customized by the player in the
	 * customization window
	 */
	private String name; // would like to use enum, but looks like I can't
	private Player registeredTo;
	private HashMap<T, Stat<T>> stats;
	private HashMap<T, Integer> bases;
	private int cooldownTime; // frames between uses of this upgradable in battle
	// not to be confused with...
	private int cooldown; // frames until this upgradable can be used in battle again
	private StatusTable inflict; // statuses that this may inflict. Each subclass handles this themself
	
	// constructors
	public AbstractUpgradable(String n){
		name = n;
		stats = new HashMap<>();
		bases = new HashMap<>();
		inflict = new StatusTable();
		cooldownTime = 0;
		cooldown = 0;
	}
	public AbstractUpgradable<T> copy(){
		return this;
	}
	
	// setters and getters
	public void setName(String s){
		name = s;
	}
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
	
	// stat stuf
	public void addStat(Stat<T> s){
		stats.put(s.getName(), s);
	}
	public void addStat(T n, double base){
		addStat(new Stat<T>(n, base));
	}
	public void addStat(T n, double base, double maxRelativeToMin){
		// maxRelativeToMin is how much the stat's value is at max.
		// EX: base of 12.5, maxRelativeToMin of 2.0 will result it 25.0 max
		addStat(new Stat<T>(n, base, maxRelativeToMin));
	}
	public Stat<T> getStat(T n){
		Stat<T> ret;
		if(!stats.containsKey(n)){
			throw new NullPointerException("Stat not found for " + registeredTo.getName() + " with name " + n);
		} else {
			ret = stats.get(n);
		}
		return ret;
	}
	
	public double getStatValue(T n){
		return getStat(n).get();
	}
	public HashMap<T, Stat<T>> getStats(){
		return stats;
	}
	
	// base stuff
	public void setBase(T statName, int value){
		bases.put(statName, value);
	}
	public int getBase(T statName){
		return bases.get(statName);
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
		cooldownTime = Master.seconds(seconds);
	}
	public int getCooldown(){
		return cooldown;
	}
	public void setToCooldown(){
		cooldown = cooldownTime;
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
	public void clearInflict(){
		inflict = new StatusTable();
	}
	public void copyInflictTo(AbstractUpgradable<T> a){
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
		for(T s : stats.keySet()){
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
