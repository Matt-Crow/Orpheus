package actions;

import java.util.ArrayList;

import entities.Entity;
import entities.Player;
import java.io.Serializable;
import static java.lang.System.out;
import util.SafeList;

/**
 * An ActionRegister is used to store OnHit- and OnUpdate-Listeners, and register them to an Entity.
 * It is used by the Player class to store Status effects and passives.
 */
public class ActionRegister implements Serializable{
	private final Entity registeredTo;
	private SafeList<OnHitListener> onHitRegister;
	private SafeList<OnHitListener> onBeHitRegister;
	private SafeList<OnHitListener> onMeleeHitRegister;
	private SafeList<OnHitListener> onBeMeleeHitRegister;
	private ArrayList<OnUpdateListener> onUpdateRegister;
	
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
		onUpdateRegister = new ArrayList<>();
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
	public void triggerOnHit(Player hit){
		OnHitEvent t = new OnHitEvent(hit, registeredTo);
        onHitRegister.forEach((ohl)->ohl.trigger(t));
	}
	public void triggerOnHitReceived(Player hitBy){
		OnHitEvent t = new OnHitEvent(registeredTo, hitBy);
		onBeHitRegister.forEach((ohl)->ohl.trigger(t));
	}
	public void tripOnMeleeHit(Player hit){
		OnHitEvent t = new OnHitEvent(hit, registeredTo);
		onMeleeHitRegister.forEach((ohl)->ohl.trigger(t));
		triggerOnHit(hit);
	}
	public void tripOnBeMeleeHit(Player hitBy){
		OnHitEvent t = new OnHitEvent(registeredTo, hitBy);
		onBeMeleeHitRegister.forEach((ohl)->ohl.trigger(t));
		triggerOnHitReceived(hitBy);
	}
	public void triggerOnUpdate(){
		for(OnUpdateListener a : onUpdateRegister){
			a.actionPerformed(new OnUpdateEvent(registeredTo));
		}
	}
	public void resetTrips(){
		onHitRegister = new SafeList<>();
		onBeHitRegister = new SafeList<>();
		onMeleeHitRegister = new SafeList<>();
		onBeMeleeHitRegister = new SafeList<>();
		onUpdateRegister = new ArrayList<>();
	}
    
    public void displayData(){
        out.println("ACTION REGISTER");
        out.println("On hit: x" + onHitRegister.length());
        out.println("On be hit: x" + onBeHitRegister.length());
        out.println("On melee hit: x" + onMeleeHitRegister.length());
        out.println("On be melee hit: x" + onBeMeleeHitRegister.length());
        out.println("On update: x" + onUpdateRegister.size());
        out.println("END ACTION REGISTER");
    }
}
