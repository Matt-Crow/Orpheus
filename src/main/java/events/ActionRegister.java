package events;

import entities.AbstractPlayer;
import entities.AbstractReactiveEntity;
import java.io.Serializable;
import static java.lang.System.out;
import util.SafeList;

/**
 * An ActionRegister is used to store OnHit- and OnUpdate-Listeners, and register them to an AbstractEntity.
 * It is used by the AbstractPlayer class to store Status effects and passives.
 */
public class ActionRegister implements Serializable{
	private final AbstractReactiveEntity registeredTo;
	private final SafeList<OnHitListener> onHitRegister;
	private final SafeList<OnHitListener> onBeHitRegister;
	private final SafeList<OnUpdateListener> onUpdateRegister;
	
    /**
     * Stores Listeners for an AbstractEntity.
     * @param e the AbstractReactiveEntity to store Listeners for.
     */
	public ActionRegister(AbstractReactiveEntity e){
		registeredTo = e;
		onHitRegister = new SafeList<>();
		onBeHitRegister = new SafeList<>();
		onUpdateRegister = new SafeList<>();
	}
    
    /**
     * adds a Listener that will fire whenever this AbstractEntity lands an attack on another.
     * @param a the listener to add.
     */
	public void addOnHit(OnHitListener a){
		onHitRegister.add(a);
	}
    
    /**
     * Adds a Listener that will fire whenever this AbstractEntity is struck by another.
     * @param a the listener to add.
     */
	public void addOnBeHit(OnHitListener a){
		onBeHitRegister.add(a);
	}
    
	public void addOnUpdate(OnUpdateListener a){
		onUpdateRegister.add(a);
	}
	public void triggerOnHit(AbstractPlayer hit){
		OnHitEvent t = new OnHitEvent(registeredTo, hit);
        onHitRegister.forEach((ohl)->ohl.trigger(t));
	}
	public void triggerOnHitReceived(AbstractPlayer hitBy){
		OnHitEvent t = new OnHitEvent(hitBy, registeredTo);
		onBeHitRegister.forEach((ohl)->ohl.trigger(t));
    }
	public void triggerOnUpdate(){
        OnUpdateEvent e = new OnUpdateEvent(registeredTo);
		onUpdateRegister.forEach((oul)->oul.trigger(e));
	}
	public void reset(){
		onHitRegister.clear();
		onBeHitRegister.clear();
		onUpdateRegister.clear();
	}
    
    public void displayData(){
        out.println("ACTION REGISTER");
        out.println("On hit: x" + onHitRegister.length());
        out.println("On be hit: x" + onBeHitRegister.length());
        out.println("On update: x" + onUpdateRegister.length());
        out.println("END ACTION REGISTER");
    }
}
