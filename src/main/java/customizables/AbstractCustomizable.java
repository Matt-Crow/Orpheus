package customizables;

import entities.AbstractPlayer;
import controllers.Master;
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
    private final CustomizableType type;
    private final String name;
	private AbstractPlayer user;
	private int cooldownTime;          // frames between uses of this upgradable in battle
	private int framesUntilUse;        // frames until this upgradable can be used in battle again
	
    private final StatusTable inflict;       // statuses that this may inflict. Each subclass handles this themself
	
	// constructors
	public AbstractCustomizable(CustomizableType t, String n){
		type = t;
        name = n;
		inflict = new StatusTable();
		cooldownTime = 0;
		framesUntilUse = 0;
	}
    
	public final String getName(){
		return name;
	}
    
    public final CustomizableType getType(){
        return type;
    }
    
	@Override
	public String toString(){
		return name;
	}
    
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
	
    /**
     * Sets the maximum frequency of how often this can be used.
     * Each subclass must still deal with this in their own way.
     * @param seconds the minimum number of seconds between each use of this.
     */
	public final void setCooldown(int seconds){
		cooldownTime = Master.seconds(seconds);
	}
    
    /**
     * Gets how long until this can be used again
     * @return how many frames until this is considered "off cooldown"
     */
	public final int getCooldown(){
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
	public final boolean onCooldown(){
		return framesUntilUse > 0;
	}
	
	// status methods. Will document after I redo StatusTable
	public final void addStatus(AbstractStatus s){
		inflict.add(s);
	}
	public final void setInflict(StatusTable s){
		clearInflict();
        s.forEach((status)->{
            inflict.add(status.copy());
        });
	}
	public final StatusTable getInflict(){
		return inflict;
	}
	public final void clearInflict(){
		inflict.clear();
	}
    
	public void copyInflictTo(AbstractCustomizable a){
		/* takes all the statuses from this upgradable's
		 * status table, and copies them to p's
		 */
        inflict.forEach((status)->{
            a.addStatus(status);
        });
	}
    
    // in battle methods. These are applied in the subclasses
	public void applyEffect(AbstractPlayer p){
		inflict.forEach((status)->{
            p.inflict(status.copy());
        });
	}
	
    /**
     * Sets this to off cooldown.
     * Make sure to call super.init() when you override!
     */
	public void init(){
		framesUntilUse = 0;
	}
	
    /**
     * Ticks down the cooldown.
     * Make sure you call super.init() when you override!
     */
	public void update(){
		framesUntilUse -= 1;
	}
    
    public abstract AbstractCustomizable copy();
    public abstract String getDescription();
}
