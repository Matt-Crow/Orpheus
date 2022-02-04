package world.customizables;

import world.entities.AbstractPlayer;
import java.io.Serializable;

/**
 * A AbstractBuildAttribute is a choice players can make when customizing the 
 * character they play as.
 */
public abstract class AbstractBuildAttribute implements Serializable{
    private final String name;
	private AbstractPlayer user;
	
	public AbstractBuildAttribute(String n){
        name = n;
	}
    
	public final String getName(){
		return name;
	}
    
    public final void setUser(AbstractPlayer p){
		user = p;
	}
    
	public final AbstractPlayer getUser(){
		return user;
	}
    
	@Override
	public String toString(){
		return String.format("BuildAttribute \"%s\"", name);
	}
    
    
    
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
