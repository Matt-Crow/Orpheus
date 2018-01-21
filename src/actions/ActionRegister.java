package actions;

import java.util.ArrayList;

import entities.Entity;
import entities.Player;

public class ActionRegister {
	private Entity registeredTo;
	private ArrayList<OnHitKey> onHitRegister;
	private ArrayList<OnHitKey> onBeHitRegister;
	private ArrayList<OnHitKey> onMeleeHitRegister;
	private ArrayList<OnHitKey> onBeMeleeHitRegister;
	private ArrayList<OnUpdateAction> onUpdateRegister;
	
	public ActionRegister(Entity e){
		registeredTo = e;
		onHitRegister = new ArrayList<>();
		onBeHitRegister = new ArrayList<>();
		onMeleeHitRegister = new ArrayList<>();
		onBeMeleeHitRegister = new ArrayList<>();
		onUpdateRegister = new ArrayList<>();
	}
	public void addOnHit(OnHitKey a){
		onHitRegister.add(a);
	}
	public void addOnBeHit(OnHitKey a){
		onBeHitRegister.add(a);
	}
	public void addOnMeleeHit(OnHitKey a){
		onMeleeHitRegister.add(a);
	}
	public void addOnBeMeleeHit(OnHitKey a){
		onBeMeleeHitRegister.add(a);
	}
	public void addOnUpdate(OnUpdateAction a){
		onUpdateRegister.add(a);
	}
	public void tripOnHit(Player hit){
		OnHitTrip t = new OnHitTrip(hit, registeredTo);
		for(OnHitKey a : onHitRegister){
			a.trip(t);
		}
	}
	public void tripOnBeHit(Player hitBy){
		OnHitTrip t = new OnHitTrip(registeredTo, hitBy);
		for(OnHitKey a : onBeHitRegister){
			a.trip(t);
		}
	}
	public void tripOnMeleeHit(Player hit){
		OnHitTrip t = new OnHitTrip(hit, registeredTo);
		for(OnHitKey a : onMeleeHitRegister){
			a.trip(t);
		}
		tripOnHit(hit);
	}
	public void tripOnBeMeleeHit(Player hitBy){
		OnHitTrip t = new OnHitTrip(registeredTo, hitBy);
		for(OnHitKey a : onBeMeleeHitRegister){
			a.trip(t);
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
