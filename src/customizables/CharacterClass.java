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
	
	public void setHPData(double HP, double regen, double wait){
		stats.add(new Stat("maxHP", 500 * HP, 2));
		stats.add(new Stat("Healing", 5 * regen));
		stats.add(new Stat("Heal rate", 20 * wait));
	}
	public void setEnergyData(double max, double epr, double er, double eph, double ephr){
		stats.add(new Stat("Max energy", 50 * max, 2));
		stats.add(new Stat("EPR", 5 * epr, 2));
		stats.add(new Stat("ER", 20 * er));
		stats.add(new Stat("EPH", 5 * eph, 2));
		stats.add(new Stat("EPHR", 5 * ephr, 2));
	}
	public void setCombatData(double armor, double attack){
		stats.add(new Stat("armor", 0.20 * armor));
		stats.add(new Stat("Attack", 20 * attack));
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
	public double calcDamage(AttackInstance attack){
		return attack.calcDamage() * (1.0 - getStatValue("armor"));
	}
	public void logDamage(AttackInstance attack){
		damageBacklog += calcDamage(attack);
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
