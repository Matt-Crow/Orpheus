package customizables;
import upgradables.Stat;
import attacks.*;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class CharacterClass extends Customizable{
	private ArrayList<Stat> stats;
	private String name;
	private Color color;
	private ArrayList<Attack> attackOptions;
	
	// initializers
	public CharacterClass(String n, Color c){
		name = n;
		color = c;
		stats = new ArrayList<>();
		attackOptions = new ArrayList<>();
		attackOptions.add(new Slash());
		attackOptions.add(new HeavyStroke());
		attackOptions.add(new WarriorsStance());
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
	//other
	public void calcStats(){
		for(Stat stat : stats){
			stat.calc();
		}
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
}
