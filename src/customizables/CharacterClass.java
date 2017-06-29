package customizables;
import upgradables.Stat;
import battle.AttackInstance;
import resources.Op;
import attacks.*;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

//ugly
public class CharacterClass extends Customizable{
	private ArrayList<Stat> stats;
	private double damageBacklog;
	private int remHP;
	private int energy;
	private String name;
	private Color color;
	private ArrayList<Attack> attackOptions;
	private Attack[] actives;
	
	public CharacterClass(String n, Color c){
		name = n;
		color = c;
		stats = new ArrayList<>();
		attackOptions = new ArrayList<>();
		actives = new Attack[3];
		attackOptions.add(new Slash());
		attackOptions.add(new HeavyStroke());
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
	public void addPossibleActive(Attack a){
		attackOptions.add(a);
	}
	public void calcStats(){
		for(Stat stat : stats){
			stat.calc();
		}
	}
	public String getName(){
		return name;
	}
	public Color getColor(){
		return color;
	}
	public int getEnergy(){
		return energy;
	}
	public String[] getAttackNames(){
		String[] ret = new String[attackOptions.size()];
		int num = 0;
		for(Attack a : attackOptions){
			ret[num] = a.getName();
			num ++;
		}
		return ret;
	}
	public ArrayList<Attack> getAttackOption(){
		return attackOptions;
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
	public void setActive(String name, int index){
		for(Attack a : attackOptions){
			if(a.getName() == name){
				actives[index] = a;
				return;
			}
		}
		actives[index] = new Slash();
	}
	public void displayPopup(int x, int y, Graphics g){
		g.setColor(Color.white);
		g.fillRect(x, y, 100, 200);
		g.setColor(Color.black);
		g.drawString(name, x + 10, y + 10);
		int statY = y + 30;
		calcStats();
		for(Stat stat : stats){
			g.drawString(stat.name + ": " + stat.get(), x, statY);
			statY += 20;
		}
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
