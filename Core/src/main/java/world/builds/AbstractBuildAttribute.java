package world.builds;

import java.util.function.Consumer;

import orpheus.core.utils.Prototype;
import orpheus.core.world.occupants.players.Player;

/**
 * A AbstractBuildAttribute is a choice players can make when customizing the 
 * character they play as.
 */
public abstract class AbstractBuildAttribute implements Prototype {
    private final String name;
	private Player user;
	
	public AbstractBuildAttribute(String n){
        name = n;
	}

	/**
	 * Calls a function on this passive's user, if the user is set
	 * @param doThis the function to call on this passive's user
	 */
	protected void withUser(Consumer<Player> doThis) {
		var user = getUser();
		if (user != null) {
			doThis.accept(user);
		}
	}
    
	@Override
	public final String getName(){
		return name;
	}
    
    public final void setUser(Player p){
		user = p;
	}
    
	public final Player getUser(){
		return user;
	}
    
	@Override
	public String toString(){
		return String.format("BuildAttribute \"%s\"", name);
	}
    
    @Override
    public abstract AbstractBuildAttribute copy();
    
    public abstract String getDescription();
    
    /**
     * Performs any initialization
     * needed prior to battle.
     */
    public abstract void init();
    
    /**
     * Called at the end of every frame
     */
    public abstract void update();
}
