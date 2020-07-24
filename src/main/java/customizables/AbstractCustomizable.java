package customizables;

import entities.AbstractPlayer;
import controllers.Settings;
import java.io.Serializable;
import statuses.*;

/**
 * The AbstractCustomizable is the base class from which 
 Actives, Passives, and CharacterClasses derive.
 * 
 * It allows these classes to store components used by all 3 of them:
 * namely, their name, registered player, stats, cooldowns, and infliction tables;
 * as well as any functionality employed by all of them.
 * 
 * The name is based on the fact that players would be able to upgrade these 3 subclasses,
 * a feature I no longer intend to implement.
 * With that said, this may be renamed in the future
 */

public abstract class AbstractCustomizable implements Serializable{
    private final String name;
	private AbstractPlayer user;
	private final StatusTable inflict; // statuses that this may inflict. Each subclass handles this themself
    private int cooldownTime;          // frames between uses of this upgradable in battle
	private int framesUntilUse;        // frames until this upgradable can be used in battle again
    
	public AbstractCustomizable(String n){
        name = n;
		inflict = new StatusTable();
		cooldownTime = 0;
		framesUntilUse = 0;
	}
    
    /*
    Name related methods
    */
    
	public final String getName(){
		return name;
	}
    
	@Override
	public String toString(){
		return name;
	}
    
    
    /*
    User related methods
    */
    
    /**
     * "Registers" a AbstractPlayer to this upgradable.
     * Used to allow this to target the player using it.
     * @param p the player to apply this to.
     */
	public final void setUser(AbstractPlayer p){
		user = p;
	}
    
    /**
     * Gets the player who this is applied to.
     * @return the player registered to this.
     */
	public final AbstractPlayer getUser(){
		return user;
	}
	
    
    /*
    Cooldown related methods
    */
    
    /**
     * Sets the maximum frequency of how often this can be used.
     * Each subclass must still deal with this in their own way.
     * @param seconds the minimum number of seconds between each use of this.
     */
	public final void setCooldownTime(int seconds){
		cooldownTime = Settings.seconds(seconds);
	}
    
    /**
     * Gets how long until this can be used again
     * @return how many frames until this is considered "off cooldown"
     */
	public final int getFramesUntilUse(){
		return framesUntilUse;
	}
    
    /**
     * Notify this upgradable that it has been used.
     */
	public final void setToCooldown(){
		framesUntilUse = cooldownTime;
	}
    
    /**
     * Gets if this should be usable.
     * @return whether or not this is "on cooldown"
     */
	public final boolean isOnCooldown(){
		return framesUntilUse > 0;
	}
	
    
    /*
    Status related methods
    */
    
	/**
     * Adds a copy of the given status to this
     * inflict table.
     * 
     * @param s 
     */
	public final void addStatus(AbstractStatus s){
		inflict.add(s);
	}
    
    /**
     * Adds copies of all the given statuses
     * to this inflict table.
     * 
     * @param ss 
     */
    public final void addStatuses(AbstractStatus[] ss){
        for(AbstractStatus s : ss){
            inflict.add(s);
        }
    }
    
    /**
     * Used to get the table of statuses associated
     * with this Customizable.
     * 
     * @return 
     */
	public final StatusTable getInflict(){
		return inflict;
	}
    
    /**
     * takes all the statuses from this upgradable's
     * status table, and copies them to a's
     * @param a 
     */
	public final void copyInflictTo(AbstractCustomizable a){
        inflict.forEach((status)->{
            a.addStatus(status);
        });
	}
    
    /**
     * Inflicts each status in this' status table on
     * the given AbstractPlayer.
     * 
     * @param p 
     */
	public final void applyEffect(AbstractPlayer p){
		inflict.forEach((status)->{
            p.inflict(status.copy());
        });
	}
	
    
    /*
    Overridable methods.
    */
    
    /**
     * Performs any initialization
     * needed prior to battle.
     */
	public final void doInit(){
		framesUntilUse = 0;
        init();
	}
	
    /**
     * Performs any updates
     * needed at the end of
     * the frame
     */
	public void doUpdate(){
		framesUntilUse -= 1;
        update();
	}
    
    /**
     * This method should return a copy of this,
     * passing the arguments used to initialize this
     * to this constructor
     * @return 
     */
    public abstract AbstractCustomizable copy();
    public abstract String getDescription();
    
    /**
     * This method is called at
     * the beginning of battle.
     */
    public abstract void init();
    
    /**
     * This method should be invoked
     * by subclasses
     */
    public abstract void trigger();
    
    /**
     * Called at the end of every frame
     */
    public abstract void update();
}
