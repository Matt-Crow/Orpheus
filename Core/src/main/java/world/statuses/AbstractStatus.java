package world.statuses;

import world.events.Terminable;
import world.events.termination.TerminationListener;
import world.entities.AbstractPlayer;
import java.io.Serializable;
import util.Number;
import java.util.function.UnaryOperator;
import util.SafeList;

/**
 * AbstractStatus is the base class for all statuses in Orpheus.
 * 
 * A status is condition which will be applied to a AbstractPlayer,
 adding itself to that AbstractPlayer's ActionRegister as either an OnHitListener, or an OnUpdateListener
 * 
 * @see ActionRegister
 * @see AbstractPlayer#inflict(statuses.AbstractStatus) 
 */
public abstract class AbstractStatus implements Serializable, Terminable, world.events.termination.Terminable{
	private final StatusName code; //the Enum of this status' name
	private final String name;
	
    private final int useBase; //used for serialization
    private final int maxUses;
    private int usesLeft;
	private final int level;
    
    private boolean hasTerminated;
    private final SafeList<TerminationListener> termListens;
    
	
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
        code = enumName;
		name = enumName.toString();
		
        useBase = Number.minMax(1, use, 3);
        maxUses = useCalc.apply(useBase);
        usesLeft = maxUses;
        
        level = Number.minMax(1, lv, 3);
        
        hasTerminated = false;
        termListens = new SafeList<>();
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
    
    public int getUsesLeft(){
        return usesLeft;
    }
    
    public int getMaxUses(){
        return maxUses;
    }
    
    /**
     * 
     * @param n
     * @param intensity
     * @param dur should be the useBase of the status, not its actual uses
     * @return 
     */
    public static AbstractStatus decode(StatusName n, int intensity, int dur){
		AbstractStatus ret = null;
		switch(n){
		case REGENERATION:
			ret = new Regeneration(intensity, dur);
			break;
		case RESISTANCE:
			ret = new Resistance(intensity, dur);
			break;
        case BURN:
            ret = new Burn(intensity, dur);
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
    
    @Override
    public void addTerminationListener(TerminationListener listen) {
        termListens.add(listen);
    }

    @Override
    public void terminate() {
        if(!hasTerminated){
            hasTerminated = true;
            termListens.forEach((TerminationListener tl)->tl.objectWasTerminated(this));
        }
    }

    @Override
    public boolean isTerminating() {
        return usesLeft <= 0;
    }
    
    public final void use(){
        usesLeft--;
        if(usesLeft <= 0){
            terminate();
        }
    }
    
    /**
     * Generally speaking, this will call p.getActionRegister().add . . .
     * <b>this method must call use() in order to update the number of uses left</b>
     * @param p the AbstractPlayer to target 
     * @see AbstractPlayer
     */
    public abstract void inflictOn(AbstractPlayer p);
    
    /**
     * Gives a brief description of the status
     * @return a description
     */
    public abstract String getDesc();
    
    public abstract AbstractStatus copy();
}