package world.statuses;

import world.events.termination.*;
import util.Number;
import java.util.function.UnaryOperator;

import orpheus.core.world.occupants.players.Player;

/**
 * AbstractStatus is the base class for all statuses in Orpheus.
 * 
 * A status is condition which will be applied to a AbstractPlayer,
 adding itself to that AbstractPlayer's ActionRegister as either an OnHitListener, or an OnUpdateListener
 * 
 * @see ActionRegister
 * @see Player#inflict(statuses.AbstractStatus) 
 */
public abstract class AbstractStatus implements Terminable {
	private final String name;
	
    private final int useBase; //used for serialization
    private final int maxUses;
    private int usesLeft;
	private final int level;
    
    private boolean hasTerminated;
    private final TerminationListeners terminationListeners = new TerminationListeners();    
    
    /**
     * @param name name of the status. Statuses of the same name overwrite each other when inflicted.
     * These next two use my standard 1-3 system, where 1 means "weak", 2 means "average", and 3 means "strong".
     * @param lv an integer (1 to 3) which designates how powerful the status is
     * @param use an integer (1 to 3) which designates how long the status lasts.
     * @param useCalc takes the use as a parameter, and produces the actual number of times the status can activate.
     * the class may need this more later
     */
	public AbstractStatus(String name, int lv, int use, UnaryOperator<Integer> useCalc){
		this.name = name;
		
        useBase = Number.minMax(1, use, 3);
        maxUses = useCalc.apply(useBase);
        usesLeft = maxUses;
        
        level = Number.minMax(1, lv, 3);
        
        hasTerminated = false;
	}
	
    /**
     * @return the name of this status. Statuses with the same name overwrite each other
     */
    public String getName() {
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
     * Checks to see if this status should replace the given other status when
     * inflicted upon a player.
     * 
     * @param other a status the other player is inflicted with, or null
     * @return whether this should replace the other status
     */
    public boolean isBetterThan(AbstractStatus other) {
        if (other == null) {
            return true; // anything is better than nothing
        }
        if (other.name != name) {
            throw new IllegalArgumentException("can only compare two statuses of the same type");
        }
        var isMoreIntense = level > other.level;
        var isNotWeaker = isMoreIntense || level == other.level;
        var lastsLonger = usesLeft > other.usesLeft;
        return isMoreIntense || (isNotWeaker && lastsLonger);
    }
    
    @Override
    public void addTerminationListener(TerminationListener listen) {
        terminationListeners.add(listen);
    }

    @Override
    public void terminate() {
        if(!hasTerminated){
            hasTerminated = true;
            terminationListeners.objectWasTerminated(this);
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
     * @see Player
     */
    public abstract void inflictOn(Player p);
    
    /**
     * Gives a brief description of the status
     * @return a description
     */
    public abstract String getDesc();
    
    public abstract AbstractStatus copy();

    @Override
    public String toString() {
        var is = "";
        for (var i = 0; i < level; i++) {
            is += 'I';
        }

        return String.format("%s %s (%d)", name, is, usesLeft);
    }
}