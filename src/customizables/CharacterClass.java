package customizables;
import upgradables.Stat;
import passives.*;
import resources.Op;

import java.util.ArrayList;

import actives.*;

import java.awt.Color;

// make this connect better with player somehow
public class CharacterClass extends Customizable{
	private ArrayList<Stat> stats;
	private String name;
	private Color color;
	private ArrayList<AbstractActive> attackOptions;
	private ArrayList<Passive> passiveOptions;
	
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
		
		attackOptions = new ArrayList<>();
		attackOptions.add(new Slash());
		attackOptions.add(new HeavyStroke());
		addPossibleActive("Warrior's Stance");
		addPossibleActive("Shield Stance");
		
		attackOptions.add(new Flurry());
		addPossibleActive("Blade Stance");
		addPossibleActive("Heal");
		addPossibleActive("RAINBOW OF DOOM");
		addPossibleActive("Tracking Projectile Test");
		addPossibleActive("Cursed Daggers");
		
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
		passiveOptions.add(new Recover());
	}
	// getters
	public String getName(){
		return name;
	}
	public Color getColor(){
		return color;
	}
	public AbstractActive[] getAttackOption(){
		AbstractActive[] ret = new AbstractActive[attackOptions.size()];
		for(int i = 0; i < attackOptions.size(); i++){
			ret[i] = attackOptions.get(i);
		}
		return ret;
	}
	public String[] getAttacksString(){
		String[] ret = new String[attackOptions.size()];
		for(int i = 0; i < attackOptions.size(); i++){
			ret[i] = attackOptions.get(i).getName();
		}
		return ret;
	}
	public Passive[] getPassiveOptions(){
		Passive[] ret = new Passive[passiveOptions.size()];
		for(int i = 0; i < passiveOptions.size(); i++){
			ret[i] = passiveOptions.get(i);
		}
		return ret;
	}
	public String[] getPassivesString(){
		String[] ret = new String[passiveOptions.size()];
		for(int i = 0; i < passiveOptions.size(); i++){
			ret[i]= passiveOptions.get(i).getName();
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
	public void addPossibleActive(AbstractActive a){
		attackOptions.add(a);
	}
	public void addPossibleActive(String n){
		try{
			attackOptions.add(AbstractActive.getActiveByName(n));
		} catch(NullPointerException e){
			Op.add(n + " not found in allActives");
			Op.dp();
		}
		/*
		AbstractActiveBlueprint bp = AbstractActiveBlueprint.getBlueprintByName(n);
		switch(bp.getType()){
		case BOOST:
			attackOptions.add(new BoostActive((BoostActiveBlueprint)bp));
			break;
		}
		*/
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
