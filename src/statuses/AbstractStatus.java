package statuses;

import entities.Player;

/**
 * AbstractStatus is the base class for all statuses in Orpheus.
 * 
 * A status is condition which will be applied to a Player,
 * and will apply an OnHitKey or OnUpdateAction of some sort to that Player at the end of each frame.
 * @see OnUpdateAction
 * @see OnHitKey
 * @see Player
 */
public abstract class AbstractStatus {
	private final StatusName code; //the Enum of this status' name
	private final String name;
	
	private final int level;
	private final int uses;
	private int usesLeft;
	// need to implement calculation
	
	private boolean shouldTerminate;
	
	public AbstractStatus(StatusName enumName, String n, int lv, int use){
		code = enumName;
		name = n;
		level = lv;
		uses = use;
		usesLeft = use;
		
		shouldTerminate = false;
	}
	
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
	
	public StatusName getStatusName(){
		return code;
	}
	public String getName(){
		return name;
	}
	public int getIntensityLevel(){
		return level;
	}
	public int getBaseUses(){
		return uses;
	}
	public int getUsesLeft(){
		return usesLeft;
    }
	
    
    /**
     * Flags that this status should terminate.
     */
	public final void terminate(){
		shouldTerminate = true;
	}
    
    /**
     * Gets if this is flagged for termination
     * @return whether or not this should terminate
     */
    public boolean getShouldTerminate(){
		return shouldTerminate;
	}
    
    /**
     * Sets this' number of uses back to the initial amount.
     */
	public final void reset(){
		shouldTerminate = false;
		usesLeft = uses;
	}
    
    /**
     * reduces the number of uses this status has left by 1.
     * if the number of uses reaches 0, the Status will be flagged for termination.
     * Note that this does not delete the status.
     */
	public final void use(){
		usesLeft -= 1;
		if(usesLeft <= 0){
			terminate();
		}
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
}