package world.entities;

import world.WorldContent;
import world.battle.Team;
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
    private Team team;
    
    public AbstractReactiveEntity(WorldContent inWorld){
        super(inWorld);
        
        actReg = new ActionRegister(this);
    }
    
    public final ActionRegister getActionRegister(){
		return actReg;
	}
    
    public final void setTeam(Team t){
		team = t;
	}
	public final Team getTeam(){
		return team;
	}
    
    /**
     * Inserts an AbstractEntity into this' EntityNode chain.
     * Since the AbstractEntity is inserted before this one, it will not be 
     * updated during this iteration of EntityManager.update
     * @param e the AbstractEntity to insert before this one
     */
    public final void spawn(AbstractEntity e){
        if(e == null){
            throw new NullPointerException();
        }
        team.add(e);
    }
    
    @Override
    public void init(){
        super.init();
        actReg.reset();
    }
    
    @Override
    public void update(){
        super.update();
        actReg.triggerOnUpdate();
    }
}
