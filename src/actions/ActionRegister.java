package actions;

import java.util.ArrayList;

import entities.Entity;
import entities.Player;
import java.io.Serializable;

/**
 * An ActionRegister is used to store OnHit- and OnUpdate-Listeners, and register them to an Entity.
 * It is used by the Player class to store Status effects and passives.
 */
public class ActionRegister implements Serializable{
	private final Entity registeredTo;
	private ArrayList<OnHitListener> onHitRegister;
	private ArrayList<OnHitListener> onBeHitRegister;
	private ArrayList<OnHitListener> onMeleeHitRegister;
	private ArrayList<OnHitListener> onBeMeleeHitRegister;
	private ArrayList<OnUpdateListener> onUpdateRegister;
	
    /**
     * Stores Listeners for an Entity.
     * @param e the Entity to store Listeners for.
     */
	public ActionRegister(Entity e){
		registeredTo = e;
		onHitRegister = new ArrayList<>();
		onBeHitRegister = new ArrayList<>();
		onMeleeHitRegister = new ArrayList<>();
		onBeMeleeHitRegister = new ArrayList<>();
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
        onHitRegister.forEach((a) -> {
            a.actionPerformed(t);
        });
	}
	public void triggerOnHitReceived(Player hitBy){
		OnHitEvent t = new OnHitEvent(registeredTo, hitBy);
		for(OnHitListener a : onBeHitRegister){
			a.actionPerformed(t);
		}
	}
	public void tripOnMeleeHit(Player hit){
		OnHitEvent t = new OnHitEvent(hit, registeredTo);
		for(OnHitListener a : onMeleeHitRegister){
			a.actionPerformed(t);
		}
		triggerOnHit(hit);
	}
	public void tripOnBeMeleeHit(Player hitBy){
		OnHitEvent t = new OnHitEvent(registeredTo, hitBy);
		for(OnHitListener a : onBeMeleeHitRegister){
			a.actionPerformed(t);
		}
		triggerOnHitReceived(hitBy);
	}
	public void triggerOnUpdate(){
		for(OnUpdateListener a : onUpdateRegister){
			a.actionPerformed(new OnUpdateEvent(registeredTo));
		}
	}
	public void resetTrips(){
		onHitRegister = new ArrayList<>();
		onBeHitRegister = new ArrayList<>();
		onMeleeHitRegister = new ArrayList<>();
		onBeMeleeHitRegister = new ArrayList<>();
		onUpdateRegister = new ArrayList<>();
	}
}
