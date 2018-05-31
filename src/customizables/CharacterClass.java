package customizables;
import upgradables.Stat;
import java.util.ArrayList;
import java.awt.Color;

// make this connect better with player somehow
public class CharacterClass extends Customizable{
	private ArrayList<Stat> stats;
	private String name;
	private Color color;
	private ArrayList<String> passiveOptions; // remove later
	
	// initializers
	public CharacterClass(String n, Color c, int HP, int energy, int dmg, int reduction, int speed){
		name = n;
		color = c;
		stats = new ArrayList<>();
		stats.add(new Stat("maxHP", 700 + 100 * HP, 2));
		stats.add(new Stat("Max energy", 12.5 * (energy + 1), 2));
		stats.add(new Stat("damage dealt modifier", 0.7 + 0.1 * dmg));
		// 1: 120%, 5: 80%
		stats.add(new Stat("damage taken modifier", 1.3 - 0.1 * reduction));
		
		stats.add(new Stat("speed", (0.7 + 0.1 * speed)));
		
		passiveOptions = new ArrayList<>();
		addPossiblePassive("Bracing");
		addPossiblePassive("Retaliation");
		addPossiblePassive("Determination");
		addPossiblePassive("Adrenaline");
		addPossiblePassive("Toughness");
		addPossiblePassive("Sharpen");
		addPossiblePassive("Escapist");
		addPossiblePassive("Sparking Strikes");
		addPossiblePassive("Momentum");
		addPossiblePassive("Leechhealer");
		addPossiblePassive("Recover");
	}
	// getters
	public String getName(){
		return name;
	}
	public Color getColor(){
		return color;
	}
	public String[] getPassiveOptions(){
		String[] ret = new String[passiveOptions.size()];
		for(int i = 0; i < passiveOptions.size(); i++){
			ret[i] = passiveOptions.get(i);
		}
		return ret;
	}
	
	public Stat getStat(String n){
		Stat ret = new Stat("STATNOTFOUND", 0);
		for(Stat stat : stats){
			if(stat.getName() == n){
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
	
	// setters
	public void addPossiblePassive(String s){
		passiveOptions.add(s);
	}
	//other
	public void calcStats(){
		for(Stat stat : stats){
			stat.calc();
		}
	}
}
