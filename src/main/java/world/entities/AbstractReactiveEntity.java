package world.entities;

import world.events.ActionRegister;

/**
 * The AbstractReactiveEntity acts as a bridge between
 * Projectile and AbstractPlayer, as the only functionality
 * they both share is the use of an action register.
 * 
 * @author Matt Crow
 */
public abstract class AbstractReactiveEntity extends AbstractEntity{
    
    private final ActionRegister actReg;
    
    public AbstractReactiveEntity(){
        super();
        
        actReg = new ActionRegister(this);
    }
    
    public final ActionRegister getActionRegister(){
		return actReg;
	}
    
    @Override
    public void init(){
        actReg.reset();
    }
    
    @Override
    public void update(){
        actReg.triggerOnUpdate();
    }
}
