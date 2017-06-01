package customizables;
import upgradables.Stat;
import battle.AttackInstance;
import java.util.ArrayList;
import java.util.Locale;

public class CharacterClass {
	private ArrayList<Stat> stats;
	private double damageBacklog;
	private int remHP;
	private int energy;
	
	public CharacterClass(){
		stats = new ArrayList<>();
	}
	
	public void setHPData(int HP, int regen, int wait){
		stats.add(new Stat("maxHP", 350 + 50 * HP, 2));
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
	public void calcStats(){
		for(Stat stat : stats){
			stat.calc();
		}
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
	public void displayStatData(){
		for(Stat stat: stats){
			stat.displayData();
		}
	}
	public void initForBattle(){
		calcStats();
		remHP = (int) getStatValue("maxHP");
		energy = (int) getStatValue("Max energy");
	}
	public void logDamage(AttackInstance attack){
		damageBacklog += attack.calcDamage();
	}
	public void depleteBacklog(){
		double damage;
		if(damageBacklog > getStatValue("maxHP")){
			damage = getStatValue("maxHP") / 100;
		} else {
			damage = damageBacklog;
		}
		remHP -= damage;
		damageBacklog -= damage;
	}
}
