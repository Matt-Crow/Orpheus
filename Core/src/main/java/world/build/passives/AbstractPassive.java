package world.build.passives;

import world.build.AbstractTriggerableAttribute;

public abstract class AbstractPassive extends AbstractTriggerableAttribute{
	/**
	 * Passives are abilities that have specific triggers, 
	 * i.e., the user does not directly trigger them:
	 * they are triggered passively
	 */
	private final boolean targetsUser;
	
	public AbstractPassive(String n, boolean b){
		super(n);
		targetsUser = b;
	}
    
    @Override
	public abstract AbstractPassive copy();
		
	public boolean getTargetsUser(){
		return targetsUser;
	}
    
    
	
    
    @Override
    public void trigger(){
        
    }
    
    @Override
    public void update(){
        super.update();
    }
}
