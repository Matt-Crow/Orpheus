package battle;

import customizables.characterClass.CharacterStatName;
import entities.Player;
import controllers.Master;
import java.io.Serializable;

/**
 * The damage backlog is used to keep track
 * of the damage a player has taken,
 * and how much damage they have "logged".
 * This logged damage is dealt over a period of time, 
 * instead of instantly, giving Players a minimum lifespan over which they will survive.
 *
 * Players lose HP at the end of every frame, 
 * based on the amount in the backlog,
 * losing at most 20% of their maximum HP every second.
 * 
 * For example,
 * if a Player has a maximum HP of 500, 
 * then is assigned 150 points of damage,
 * they will take 100 damage over the first second after receiving the damage,
 * then 50 damage over the half second after that.
 * 
 * Every second, the Player regenerates 5% of their maximum HP.
 */
public final class DamageBacklog implements Serializable{
	private final Player registeredTo;
    private final int maxHP;
	private int remHP;
	private int timeSinceLastHeal; //number of frames since the user last regenerated health.
	private int dmg; //backlogged damage
    private final double baseFilter;
	private double secondaryFilter; //this is altered by some statuses. Resets every frame
    //The maximum percentage of the player's max HP that can be depleted per frame is (maxHP * baseFilter * secondaryFilter) 
	
	public DamageBacklog(Player register){
		registeredTo = register;
		dmg = 0;
		baseFilter = 1.0 / Master.seconds(5);
        secondaryFilter = 1.0;
		maxHP = (int) register.getStatValue(CharacterStatName.HP);
		remHP = maxHP;
		timeSinceLastHeal = 0;
	}
	public int getHP(){
		return remHP;
	}
	public double getHPPerc(){
		return remHP / registeredTo.getStatValue(CharacterStatName.HP) * 100;
	}
	public void applyFilter(double f){
		secondaryFilter *= f;
	}
	public void log(int damage){
		dmg += damage;
	}
	public void logPercentageDamage(double percent){
		log((int) (registeredTo.getStatValue(CharacterStatName.HP) * (percent / 100)));
	}
	public void deplete(){
		if(dmg <= 0){
			return;
		}
		int damage;
		if(dmg > registeredTo.getStatValue(CharacterStatName.HP) * baseFilter * secondaryFilter){
			damage = (int) (registeredTo.getStatValue(CharacterStatName.HP) * baseFilter * secondaryFilter);
		} else {
			damage = dmg;
		}
		remHP -= damage;
		dmg -= damage;
		
		if(remHP <= 0){
			registeredTo.terminate();
		}
	}
	public void heal(int amount){
		if(!((Master.DISABLEHEALING) || (remHP == maxHP))){
			remHP += amount;
			if(remHP > maxHP){
				remHP = maxHP;
			}
		}
	}
	public void healPerc(double percent){
		heal((int) (maxHP * (percent / 100)));
	}
	public void update(){
		/*
		Op.add("Before updating backlog for " + registeredTo.getName() + ":");
		Op.add("*HP remaining: " + remHP);
		Op.add("*Backlog: " + dmg);
		Op.add("*Backlog filter: " + filter);
		*/
		deplete();
		/*
		Op.add("After updating backlog: ");
		Op.add("*HP remaining: " + remHP);
		Op.add("*Backlog: " + dmg);
		Op.add("*Backlog filter: " + filter);
		
		Op.dp();
		*/
		secondaryFilter = 1.0;
		
		timeSinceLastHeal += 1;
		if(timeSinceLastHeal >= Master.seconds(1)){
			timeSinceLastHeal = 0;
			healPerc(3);
		}
	}
}
