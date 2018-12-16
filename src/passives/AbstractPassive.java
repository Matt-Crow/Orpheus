package passives;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import actions.*;
import statuses.StatusTable;
import entities.Player;
import resources.Op;
import resources.Random;
import upgradables.AbstractUpgradable;

public abstract class AbstractPassive extends AbstractUpgradable<PassiveStatName>{
	/**
	 * Passives are abilities that have specific triggers, 
	 * i.e., the user does not directly trigger them:
	 * they are triggered passively
	 */
	private PassiveType type; // used when upcasting
	private boolean targetsUser;
	
	private static HashMap<String, AbstractPassive> allPassives = new HashMap<>();
	
	public AbstractPassive(PassiveType t, String n, boolean b){
		super(n);
		type = t;
		targetsUser = b;
	}
	public AbstractPassive copy(){
		// DO NOT INVOKE THIS
		return this;
	}
	
	// static methods
	public static void addPassive(AbstractPassive p){
		allPassives.put(p.getName().toUpperCase(), p);
	}
	public static void addPassives(AbstractPassive[] ps){
		for(AbstractPassive p : ps){
			addPassive(p);
		}
	}
	public static AbstractPassive getPassiveByName(String n){
		AbstractPassive ret = allPassives.getOrDefault(n.toUpperCase(), allPassives.get("SLASH"));
		if(ret.getName().toUpperCase().equals("SLASH") && !n.toUpperCase().equals("SLASH")){
			Op.add("No passive was found with name " + n + " in AbstractPassive.getPassiveByName");
			Op.dp();
		}
		return ret;
	}
	public static AbstractPassive[] getAll(){
		AbstractPassive[] ret = new AbstractPassive[allPassives.size()];
		Collection<AbstractPassive> values = allPassives.values();
		int i = 0;
		for(AbstractPassive ap : values){
			ret[i] = ap;
			i++;
		}
		return ret;
	}
	public static String[] getAllNames(){
		String[] ret = new String[allPassives.size()];
		Set<String> keys = allPassives.keySet();
		int i = 0;
		for(String key : keys){
			ret[i] = key;
			i++;
		}
		return ret;
	}
	
	public void setStat(PassiveStatName n, int value){
		Op.add("Method addStat is not defined for class " + getClass().getName());
		Op.dp();
	}
	
	// setters / getters
	public PassiveType getType(){
		return type;
	}
	public boolean getTargetsUser(){
		return targetsUser;
	}
	
	// in battle methods. These are applied in the subclasses
	public void applyEffect(Player p){
		StatusTable inf = getInflict();
		for(int i = 0; i < inf.getSize(); i++){
			p.inflict(inf.getNameAt(i), inf.getIntensityAt(i), inf.getDurationAt(i));
		}
	}
	public OnHitListener getKey(){
		OnHitListener a = new OnHitListener(){
            @Override
			public void actionPerformed(OnHitEvent t){
				StatusTable inf = getInflict();
				for(int i = 0; i < inf.getSize(); i++){
					if(Random.chance(inf.getChanceAt(i))){
						if(getTargetsUser()){
							getRegisteredTo().inflict(inf.getNameAt(i), inf.getIntensityAt(i), inf.getDurationAt(i));
						} else {
							((Player)t.getWasHit()).inflict(inf.getNameAt(i), inf.getIntensityAt(i), inf.getDurationAt(i));
						}
					}
				}
			}
		};
		return a;
	}
	public void update(){
		
	}
}
