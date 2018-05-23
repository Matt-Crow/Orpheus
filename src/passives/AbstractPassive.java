package passives;

import java.util.ArrayList;

import actions.OnHitKey;
import actions.OnHitTrip;
import statuses.StatusName;
import statuses.StatusTable;
import entities.Player;
import resources.Random;
import upgradables.AbstractUpgradable;

public abstract class AbstractPassive extends AbstractUpgradable{
	private PassiveType type;
	private StatusTable inflict;
	private boolean targetsUser;
	private int chance;
	
	private static ArrayList<AbstractPassive> allPassives = new ArrayList<>();
	
	public AbstractPassive(PassiveType t, String n, boolean b){
		super(n);
		type = t;
		inflict = new StatusTable();
		targetsUser = b;
		chance = 100;
	}
	public AbstractPassive copy(){
		// DO NOT INVOKE THIS
		return this;
	}
	
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
	
	public PassiveType getType(){
		return type;
	}
	
	public void addStatus(StatusName n, int i, int d){
		inflict.add(n, i, d);
	}
	public StatusTable getInflict(){
		return inflict;
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
	
	public void applyEffect(Player p){
		for(int i = 0; i < inflict.getSize(); i++){
			p.inflict(inflict.getNameAt(i), inflict.getIntensityAt(i), inflict.getDurationAt(i));
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
