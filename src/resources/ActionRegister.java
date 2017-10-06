package resources;

import java.util.ArrayList;
import entities.Entity;
import entities.Player;

public class ActionRegister {
	private Entity registeredTo;
	private ArrayList<OnHitAction> onHitRegister;
	private ArrayList<OnHitAction> onBeHitRegister;
	private ArrayList<OnHitAction> onMeleeHitRegister;
	private ArrayList<OnHitAction> onBeMeleeHitRegister;
	private ArrayList<OnUpdateAction> onUpdateRegister;
	
	public ActionRegister(Entity e){
		registeredTo = e;
		onHitRegister = new ArrayList<>();
		onBeHitRegister = new ArrayList<>();
		onMeleeHitRegister = new ArrayList<>();
		onBeMeleeHitRegister = new ArrayList<>();
		onUpdateRegister = new ArrayList<>();
	}
	public void addOnHit(OnHitAction a){
		onHitRegister.add(a);
	}
	public void addOnBeHit(OnHitAction a){
		onBeHitRegister.add(a);
	}
	public void addOnMeleeHit(OnHitAction a){
		onMeleeHitRegister.add(a);
	}
	public void addOnBeMeleeHit(OnHitAction a){
		onBeMeleeHitRegister.add(a);
	}
	public void addOnUpdate(OnUpdateAction a){
		onUpdateRegister.add(a);
	}
	public void tripOnHit(Player hit){
		for(OnHitAction a : onHitRegister){
			a.setHitter(registeredTo);
			a.setHit(hit);
			a.trip();
		}
	}
	public void tripOnBeHit(Player hitBy){
		for(OnHitAction a : onBeHitRegister){
			a.setHitter(hitBy);
			a.setHit((Player) registeredTo);
			a.trip();
		}
	}
	public void tripOnMeleeHit(Player hit){
		for(OnHitAction a : onMeleeHitRegister){
			a.setHitter(registeredTo);
			a.setHit(hit);
			a.trip();
		}
		tripOnHit(hit);
	}
	public void tripOnBeMeleeHit(Player hitBy){
		for(OnHitAction a : onBeMeleeHitRegister){
			a.setHitter(registeredTo);
			a.setHit((Player) registeredTo);
			a.trip();
		}
		tripOnBeHit(hitBy);
	}
	public void tripOnUpdate(){
		for(OnUpdateAction a : onUpdateRegister){
			a.trip();
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
