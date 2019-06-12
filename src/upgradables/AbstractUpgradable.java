package upgradables;

import actives.AbstractActive;
import serialization.JsonSerialable;
import java.util.*;
import entities.Player;
import controllers.Master;
import customizables.CharacterClass;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;
import passives.AbstractPassive;
import serialization.JsonTest;
import statuses.*;

//T is an enum

/**
 * The AbstractUpgradable is the base class from which 
 * Actives, Passives, and CharacterClasses derive.
 * 
 * It allows these classes to store components used by all 3 of them:
 * namely, their name, registered player, stats, cooldowns, and infliction tables;
 * as well as any functionality employed by all of them.
 * 
 * The name is based on the fact that players would be able to upgrade these 3 subclasses,
 * a feature I no longer intend to implement.
 * With that said, this may be renamed in the future
 * @param <T> an enum, the type of stats this class will use
 * for example, the AbstractActive class extends AbstractUpgradable&lt;ActiveStatName&gt;
 */

public abstract class AbstractUpgradable<T> implements JsonSerialable{
    public final UpgradableType upgradableType;
    private String name;
	private Player registeredTo;
	private final HashMap<T, Double> stats;
    
	private final HashMap<T, Integer> bases;
	private int cooldownTime;          // frames between uses of this upgradable in battle
	private int framesUntilUse;        // frames until this upgradable can be used in battle again
	
    private StatusTable inflict;       // statuses that this may inflict. Each subclass handles this themself
	
	// constructors
	public AbstractUpgradable(UpgradableType t, String n){
		upgradableType = t;
        name = n;
		stats = new HashMap<>();
		bases = new HashMap<>();
		inflict = new StatusTable();
		cooldownTime = 0;
		framesUntilUse = 0;
	}
    
    public static void loadAll(){
        CharacterClass.loadAll();
        AbstractActive.loadAll();
        AbstractPassive.loadAll();
    }
    
	// setters and getters
	public final void setName(String s){
		name = s;
	}
	public final String getName(){
		return name;
	}
    
	@Override
	public final String toString(){
		return name;
	}
    
    /**
     * "Registers" a Player to this upgradable.
     * Used to allow this to target the player using it.
     * @param p the player to apply this to.
     */
	public final void registerTo(Player p){
		registeredTo = p;
	}
    
    /**
     * Gets the player who this is applied to.
     * @return the player registered to this.
     */
	public final Player getRegisteredTo(){
		return registeredTo;
	}
	
	/**
     * Adds a stat to this upgradable.
     * @param name the enum value of the name of this stat.
     * @param base the integer value used to calculate the stat value. Used for saving the stat to a file. 
     * For example, value may be calculated as 
     * <br>
     * {@code base * 100 + 700}
     * <br>
     * so the value can be recalculated from the base, so I can change the calculation formula
     * @param value the value of the stat.
     */
	public final void addStat(T name, int base, double value){
		stats.put(name, value);
        bases.put(name, base);
    }
	
    /**
     * Gets the value of a stat with the given name,
     * throws a NullPointerException if the stat doesn't exist.
     * @param n the name of the stat to return.
     * @return the value for the stat.
     */
	public final double getStatValue(T n){
        if(!stats.containsKey(n)){
            throw new NullPointerException("Stat not found for " + registeredTo.getName() + " with name " + n.toString());
        }
		return stats.get(n);
	}
    
    public final HashMap<Enum, Integer> getBases(){
        return (HashMap<Enum, Integer>)bases.clone();
    }
    
    /**
     * Returns the base value entered into a formula to produce a stat's value.
     * @param statName the name of the stat to get the base for.
     * @return the base value used to calculate a stat.
     */
	public final int getBase(T statName){
		return bases.get(statName);
	}
    
    /**
     * Returns all of this upgradable's stat bases.
     * May remove later.
     * @return the values used to generate this' stats.
     */
	public final int[] getAllBaseValues(){
		int[] ret = new int[bases.size()];
		Collection<Integer> values = bases.values();
		int i = 0;
		for(Integer value : values){
			ret[i] = value;
			i++;
		}
		return ret;
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
	public int getCooldown(){
		return framesUntilUse;
	}
    
    /**
     * Notify this upgradable that it has been used.
     */
	public void setToCooldown(){
		framesUntilUse = cooldownTime;
	}
    
    /**
     * Gets if this should be usable.
     * @return whether or not this is "on cooldown"
     */
	public boolean onCooldown(){
		return framesUntilUse > 0;
	}
	
	// status methods. Will document after I redo StatusTable
	public void addStatus(AbstractStatus s){
		inflict.add(s);
	}
	public void setInflict(StatusTable s){
		inflict = s.copy();
	}
	public StatusTable getInflict(){
		return inflict;
	}
	public void clearInflict(){
		inflict = new StatusTable();
	}
	public void copyInflictTo(AbstractUpgradable<T> a){
		/* takes all the statuses from this upgradable's
		 * status table, and copies them to p's
		 */
		for(int i = 0; i < inflict.getSize(); i++){
			a.addStatus(inflict.getStatusAt(i));
		}
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
    /*
    //remove this once I update serialization
    public static String getTypeFrom(JsonObject obj){
        if(!obj.containsKey("type")){
            throw new JsonException("Json Object is missing key 'type'");
        }
        return obj.getString("type");
    }*/
    
    public abstract AbstractUpgradable<T> copy();
    public abstract String getDescription();
}
