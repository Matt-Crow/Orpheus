package actions;

import entities.Entity;
import entities.AbstractPlayer;
import java.io.Serializable;
import static java.lang.System.out;
import util.SafeList;

/**
 * An ActionRegister is used to store OnHit- and OnUpdate-Listeners, and register them to an Entity.
 * It is used by the AbstractPlayer class to store Status effects and passives.
 */
public class ActionRegister implements Serializable{
	private final Entity registeredTo;
	private final SafeList<OnHitListener> onHitRegister;
	private final SafeList<OnHitListener> onBeHitRegister;
	private final SafeList<OnHitListener> onMeleeHitRegister;
	private final SafeList<OnHitListener> onBeMeleeHitRegister;
	private final SafeList<OnUpdateListener> onUpdateRegister;
	
    /**
     * Stores Listeners for an Entity.
     * @param e the Entity to store Listeners for.
     */
	public ActionRegister(Entity e){
		registeredTo = e;
		onHitRegister = new SafeList<>();
		onBeHitRegister = new SafeList<>();
		onMeleeHitRegister = new SafeList<>();
		onBeMeleeHitRegister = new SafeList<>();
		onUpdateRegister = new SafeList<>();
	}
    
    /**
     * adds a Listener that will fire whenever this Entity lands an attack on another.
     * @param a the listener to add.
     */
	public void addOnHit(OnHitListener a){
		onHitRegister.add(a);
	}
    
    /**
     * Adds a Listener that will fire whenever this Entity is struck by another.
     * @param a the listener to add.
     */
	public void addOnBeHit(OnHitListener a){
		onBeHitRegister.add(a);
	}
    
    /**
     * adds a Listener that will fire whenever this Entity lands an attack on another.
     * @param a the listener to add.
     */
	public void addOnMeleeHit(OnHitListener a){
		onMeleeHitRegister.add(a);
	}
    
    /**
     * Adds a 
     * @param a 
     */
	public void addOnBeMeleeHit(OnHitListener a){
		onBeMeleeHitRegister.add(a);
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
	public void tripOnMeleeHit(AbstractPlayer hit){
		OnHitEvent t = new OnHitEvent(registeredTo, hit);
		onMeleeHitRegister.forEach((ohl)->ohl.trigger(t));
		triggerOnHit(hit);
	}
	public void tripOnBeMeleeHit(AbstractPlayer hitBy){
		OnHitEvent t = new OnHitEvent(hitBy, registeredTo);
		onBeMeleeHitRegister.forEach((ohl)->ohl.trigger(t));
		triggerOnHitReceived(hitBy);
	}
	public void triggerOnUpdate(){
        OnUpdateEvent e = new OnUpdateEvent(registeredTo);
		onUpdateRegister.forEach((oul)->oul.trigger(e));
	}
	public void reset(){
		onHitRegister.clear();
		onBeHitRegister.clear();
		onMeleeHitRegister.clear();
		onBeMeleeHitRegister.clear();
		onUpdateRegister.clear();
	}
    
    public void displayData(){
        out.println("ACTION REGISTER");
        out.println("On hit: x" + onHitRegister.length());
        out.println("On be hit: x" + onBeHitRegister.length());
        out.println("On melee hit: x" + onMeleeHitRegister.length());
        out.println("On be melee hit: x" + onBeMeleeHitRegister.length());
        out.println("On update: x" + onUpdateRegister.length());
        out.println("END ACTION REGISTER");
    }
}
