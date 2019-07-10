package statuses;

import actions.Triggerable;
import entities.Player;
import java.io.Serializable;
import java.util.function.Consumer;
import util.Number;
import java.util.function.UnaryOperator;

/**
 * AbstractStatus is the base class for all statuses in Orpheus.
 * 
 * A status is condition which will be applied to a Player,
 * and will apply an OnHitKey or OnUpdateAction of some sort to that Player at the end of each frame.
 * @see OnUpdateAction
 * @see OnHitKey
 * @see Player
 */
// so that when it is deleted from player's action register, it is also removed from their status list
public abstract class AbstractStatus extends Triggerable<Player> implements Serializable{
	private final StatusName code; //the Enum of this status' name
	private final String name;
	
    private final int useBase; //used for serialization
        
	private final int level;
	
	private boolean shouldTerminate;
	
    /**
     * 
     * @param enumName the enum corresponding to this status. Used for serialization.
     * These next two use my standard 1-3 system, where 1 means "weak", 2 means "average", and 3 means "strong".
     * @param lv an integer (1 to 3) which designates how powerful the status is
     * @param use an integer (1 to 3) which designates how long the status lasts.
     * @param useCalc takes the use as a parameter, and produces the actual number of times the status can activate.
     * the class may need this more later
     */
	public AbstractStatus(StatusName enumName, int lv, int use, UnaryOperator<Integer> useCalc){
		super(useCalc.apply(Number.minMax(1, use, 3)));
        code = enumName;
		name = enumName.toString();
		
        useBase = Number.minMax(1, use, 3);
        
        level = Number.minMax(1, lv, 3);
		
		shouldTerminate = false;
	}
	
	public StatusName getStatusName(){
		return code;
	}
	public String getName(){
		return name;
	}
	public int getIntensityLevel(){
		return level;
	}
    
    /**
     * Gets the number that was passed 
     * into the constructor to produce
     * this status. Used for copying and serialization,
     * not to be confused with getBaseUses
     * @return an integer (1 - 3) which was passed into this object's constructor
     */
    public int getBaseParam(){
        return useBase;
    }
    
    /**
     * 
     * @param n
     * @param intensity
     * @param dur should be the useBase of the status, not its actual uses
     * @return 
     */
    public static AbstractStatus decode(StatusName n, int intensity, int dur){
		AbstractStatus ret = new Strength(1, 1);
		switch(n){
		case CHARGE:
			ret = new Charge(intensity, dur);
			break;
		case REGENERATION:
			ret = new Regeneration(intensity, dur);
			break;
		case RESISTANCE:
			ret = new Resistance(intensity, dur);
			break;
		case RUSH:
			ret = new Rush(intensity, dur);
			break;
		case STRENGTH:
			ret = new Strength(intensity, dur);
			break;
		case STUN:
			ret = new Stun(intensity, dur);
			break;
		default:
			throw new NullPointerException("Status not found: " + n);	
		}
		return ret;
	}
    
    /**
     * Generally speaking, this will call p.getActionRegister().add . . .
     * @param p the Player to target 
     * @see Player
     */
    public abstract void inflictOn(Player p);
    
    /**
     * Gives a brief description of the status
     * @return a description
     */
    public abstract String getDesc();
    
    public abstract AbstractStatus copy();
}