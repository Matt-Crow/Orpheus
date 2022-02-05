package world.battle;

import world.build.characterClass.CharacterStatName;
import world.entities.AbstractPlayer;
import util.Settings;
import java.io.Serializable;

/**
 * The damage backlog is used to keep track of the damage a player has taken,
 * and how much damage they have "logged". This logged damage is dealt over a
 * period of time, instead of instantly, giving Players a minimum lifespan over
 * which they will survive.
 *
 * Players lose HP at the end of every frame, based on the amount in the
 * backlog, losing at most a given percentage of their maximum HP every second.
 *
 * For example, if a AbstractPlayer has a maximum HP of 500 and a minimum lifespan of 5 seconds, 
 * then is assigned 150 points of damage, they will take 100 damage over the first second after
 * receiving the damage, then 50 damage over the half second after that.
 *
 * Every second, the AbstractPlayer regenerates 3% of their maximum HP.
 */
public final class DamageBacklog implements Serializable{
	private final AbstractPlayer registeredTo;
    private int maxHP;
	private int remHP;
	private int timeSinceLastHeal; //number of frames since the user last regenerated health.
	private int backlog; //backlogged damage
    private final double baseFilter;
	private double secondaryFilter; //this is altered by some statuses. Resets every frame
    //The maximum percentage of the player's max HP that can be depleted per frame is (maxHP * baseFilter * secondaryFilter) 
	
    /**
     * 
     * @param register the player this backlog is attached to.
     * @param minLifespan the minimum number of seconds this player can survive.
     */
	public DamageBacklog(AbstractPlayer register, int minLifespan){
		registeredTo = register;
		baseFilter = 1.0 / Settings.seconds(minLifespan);
	}
    
    public final void init(){
        backlog = 0;
        secondaryFilter = 1.0;
		maxHP = (int) registeredTo.getStatValue(CharacterStatName.HP);
		remHP = maxHP;
		timeSinceLastHeal = 0;
    }
    
    /**
     * 
     * @return the number of hit points this' player has remaining 
     */
	public int getHP(){
		return remHP;
	}
    
    /**
     * 
     * @return the percentage of this' player's maximum HP they have remaining,
     * multiplied by 100 (returns between 0.0 and 100.0)
     */
	public double getHPPerc(){
		return remHP / registeredTo.getStatValue(CharacterStatName.HP) * 100;
	}
	public void applyFilter(double f){
		secondaryFilter *= f;
	}
    
    /**
     * Adds to the backlog of damage this' player
     * should take over time.
     * 
     * @param damage the number of hit points this' player should lose over time. 
     */
	public void log(int damage){
		backlog += damage;
        if(backlog > maxHP){
            backlog = maxHP;
        }
	}
    
    /**
     * 
     * @param percent what percent of this' player's HP they should lose over time (between 0.0 and 100.0) 
     */
	public void logPercentageDamage(double percent){
		log((int) (registeredTo.getStatValue(CharacterStatName.HP) * (percent / 100)));
	}
    
    /**
     * Removes all uninflicted damage
     * from the damage backlog.
     */
    public final void clearBacklog(){
        backlog = 0;
    }
    
	private void deplete(){
		if(backlog <= 0){
			return;
		}
		int damage;
		if(backlog > registeredTo.getStatValue(CharacterStatName.HP) * baseFilter * secondaryFilter){
			damage = (int) (registeredTo.getStatValue(CharacterStatName.HP) * baseFilter * secondaryFilter);
		} else {
			damage = backlog;
		}
		remHP -= damage;
		backlog -= damage;
		
		if(remHP <= 0){
			registeredTo.terminate();
		}
	}
	public void heal(int amount){
		if(!((Settings.DISABLEHEALING) || (remHP == maxHP))){
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
		Op.add("*Backlog: " + backlog);
		Op.add("*Backlog filter: " + filter);
		*/
		deplete();
		/*
		Op.add("After updating backlog: ");
		Op.add("*HP remaining: " + remHP);
		Op.add("*Backlog: " + backlog);
		Op.add("*Backlog filter: " + filter);
		
		Op.dp();
		*/
		secondaryFilter = 1.0;
		
		timeSinceLastHeal += 1;
		if(timeSinceLastHeal >= Settings.seconds(1)){
			timeSinceLastHeal = 0;
			healPerc(3);
		}
	}
}
