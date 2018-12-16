package actions;

import java.util.ArrayList;

import entities.Entity;
import entities.Player;

public class ActionRegister {
	private Entity registeredTo;
	private ArrayList<OnHitListener> onHitRegister;
	private ArrayList<OnHitListener> onBeHitRegister;
	private ArrayList<OnHitListener> onMeleeHitRegister;
	private ArrayList<OnHitListener> onBeMeleeHitRegister;
	private ArrayList<OnUpdateListener> onUpdateRegister;
	
	public ActionRegister(Entity e){
		registeredTo = e;
		onHitRegister = new ArrayList<>();
		onBeHitRegister = new ArrayList<>();
		onMeleeHitRegister = new ArrayList<>();
		onBeMeleeHitRegister = new ArrayList<>();
		onUpdateRegister = new ArrayList<>();
	}
	public void addOnHit(OnHitListener a){
		onHitRegister.add(a);
	}
	public void addOnBeHit(OnHitListener a){
		onBeHitRegister.add(a);
	}
	public void addOnMeleeHit(OnHitListener a){
		onMeleeHitRegister.add(a);
	}
	public void addOnBeMeleeHit(OnHitListener a){
		onBeMeleeHitRegister.add(a);
	}
	public void addOnUpdate(OnUpdateListener a){
		onUpdateRegister.add(a);
	}
	public void tripOnHit(Player hit){
		OnHitEvent t = new OnHitEvent(hit, registeredTo);
		for(OnHitListener a : onHitRegister){
			a.actionPerformed(t);
		}
	}
	public void tripOnBeHit(Player hitBy){
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
		tripOnHit(hit);
	}
	public void tripOnBeMeleeHit(Player hitBy){
		OnHitEvent t = new OnHitEvent(registeredTo, hitBy);
		for(OnHitListener a : onBeMeleeHitRegister){
			a.actionPerformed(t);
		}
		tripOnBeHit(hitBy);
	}
	public void tripOnUpdate(){
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
