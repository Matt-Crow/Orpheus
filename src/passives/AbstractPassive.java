package passives;

import java.util.ArrayList;

import actions.OnHitKey;
import actions.OnHitTrip;
import statuses.StatusTable;
import entities.Player;
import resources.Random;
import upgradables.AbstractUpgradable;

public abstract class AbstractPassive extends AbstractUpgradable{
	/**
	 * Passives are abilities that have specific triggers, 
	 * i.e., the user does not directly trigger them:
	 * they are triggered passively
	 */
	private PassiveType type; // used when upcasting
	private boolean targetsUser;
	private int chance; // move to status table?
	
	private static ArrayList<AbstractPassive> allPassives = new ArrayList<>();
	
	public AbstractPassive(PassiveType t, String n, boolean b){
		super(n);
		type = t;
		targetsUser = b;
		chance = 100;
	}
	public AbstractPassive copy(){
		// DO NOT INVOKE THIS
		return this;
	}
	
	// static methods
	public static void addPassive(AbstractPassive p){
		allPassives.add(p);
	}
	public static void addPassives(AbstractPassive[] ps){
		for(AbstractPassive p : ps){
			addPassive(p);
		}
	}
	public static AbstractPassive getPassiveByName(String n){
		AbstractPassive ret = allPassives.get(0);
		boolean found = false;
		for(int i = 0; i < allPassives.size() && !found; i++){
			if(allPassives.get(i).getName().equals(n)){
				ret = allPassives.get(i);
				found = true;
			}
		}
		if(!found){
			throw new NullPointerException("No passive was found with name " + n);
		}
		return ret;
	}
	
	// setters / getters
	public PassiveType getType(){
		return type;
	}
	public boolean getTargetsUser(){
		return targetsUser;
	}
	public void setChance(int c){
		chance = c;
	}
	public int getChance(){
		return chance;
	}
	
	// in battle methods. These are applied in the subclasses
	public void applyEffect(Player p){
		StatusTable inf = getInflict();
		for(int i = 0; i < inf.getSize(); i++){
			p.inflict(inf.getNameAt(i), inf.getIntensityAt(i), inf.getDurationAt(i));
		}
	}
	public OnHitKey getKey(){
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				if(Random.chance(chance)){
					if(getTargetsUser()){
						applyEffect(getRegisteredTo());
					} else {
						applyEffect((Player)t.getHit());
					}
				}
			}
		};
		return a;
	}
	public void update(){
		
	}
}
