package customizables.passives;

import java.io.Serializable;
import customizables.AbstractCustomizable;

public abstract class AbstractPassive extends AbstractCustomizable implements Serializable{
	/**
	 * Passives are abilities that have specific triggers, 
	 * i.e., the user does not directly trigger them:
	 * they are triggered passively
	 */
	private final PassiveType type; // used when upcasting
	private final boolean targetsUser;
	
	public AbstractPassive(PassiveType t, String n, boolean b){
		super(n);
		type = t;
		targetsUser = b;
	}
    
    @Override
	public abstract AbstractPassive copy();
		
	// setters / getters
	public PassiveType getPassiveType(){
		return type;
	}
	public boolean getTargetsUser(){
		return targetsUser;
	}
    
    @Override
    public void trigger(){
        
    }
    
    @Override
    public void update(){
        
    }
}
