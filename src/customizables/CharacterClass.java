package customizables;
import upgradables.Stat;
import attacks.*;
import passives.*;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class CharacterClass extends Customizable{
	private ArrayList<Stat> stats;
	private String name;
	private Color color;
	private ArrayList<Attack> attackOptions;
	private ArrayList<Passive> passiveOptions;
	
	// initializers
	public CharacterClass(String n, Color c){
		name = n;
		color = c;
		stats = new ArrayList<>();
		attackOptions = new ArrayList<>();
		attackOptions.add(new Slash());
		attackOptions.add(new HeavyStroke());
		attackOptions.add(new WarriorsStance());
		attackOptions.add(new ShieldStance());
		attackOptions.add(new BladeStance());
		attackOptions.add(new Flurry());
		
		passiveOptions = new ArrayList<>();
		passiveOptions.add(new Bracing());
		passiveOptions.add(new Retaliation());
		passiveOptions.add(new Determination());
		passiveOptions.add(new Revitalize());
		passiveOptions.add(new Adrenaline());
		passiveOptions.add(new Toughness());
		passiveOptions.add(new Sharpen());
		passiveOptions.add(new Escapist());
		passiveOptions.add(new SparkingStrikes());
		passiveOptions.add(new Momentum());
		passiveOptions.add(new Leechhealer());
	}
	public void setHPData(int HP, int regen, int wait){
		stats.add(new Stat("maxHP", 350 + 50 * HP));
		stats.add(new Stat("Healing", 3.75 + 1.25 * regen));
		stats.add(new Stat("Heal rate", 35 - 5 * wait));
	}
	public void setEnergyData(int max, int epr, int er, int eph, int ephr){
		stats.add(new Stat("Max energy", 12.5 * (max + 1), 2));
		stats.add(new Stat("EPR", epr, 2));
		stats.add(new Stat("ER", 35 - 5 * er));
		stats.add(new Stat("EPH", eph + 2, 2));
		stats.add(new Stat("EPHR", ephr + 2, 2));
	}
	// getters
	public String getName(){
		return name;
	}
	public Color getColor(){
		return color;
	}
	public ArrayList<Attack> getAttackOption(){
		return attackOptions;
	}
	public ArrayList<Passive> getPassiveOptions(){
		return passiveOptions;
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
	// setters
	public void addPossibleActive(Attack a){
		attackOptions.add(a);
	}
	public void addPossiblePassive(Passive p){
		passiveOptions.add(p);
	}
	//other
	public void calcStats(){
		for(Stat stat : stats){
			stat.calc();
		}
	}
}
