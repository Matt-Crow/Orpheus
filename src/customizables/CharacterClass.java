package customizables;
import upgradables.Stat;
import java.util.ArrayList;

public class CharacterClass {
	private ArrayList<Stat> stats;
	
	public CharacterClass(){
		stats = new ArrayList<>();
	}
	
	public void setHPData(double HP, double regen, double wait){
		stats.add(new Stat("HP", 500 * HP, 2));
		stats.add(new Stat("Healing", 5 * regen));
		stats.add(new Stat("Heal rate", 20 * wait));
	}
	public void setEnergyData(double max, double epr, double er, double eph, double epbh){
		stats.add(new Stat("Max energy", 50 * max, 2));
		stats.add(new Stat("Energy per rate", 5 * epr, 2));
		stats.add(new Stat("Energy rate", 20 * er));
		stats.add(new Stat("Energy per hit", 5 * eph, 2));
		stats.add(new Stat("Energy per be hit", 5 * epbh, 2));
	}
	public void calcStats(){
		for(Stat stat : stats){
			stat.calc();
		}
	}
	public Stat getStat(String n){
		for(Stat stat : stats){
			if(stat.name == n){
				return stat;
			}
		}
		return new Stat("StatNotFound", 0);
	}
	public void displayStatData(){
		for(Stat stat: stats){
			stat.displayData();
		}
	}
}
