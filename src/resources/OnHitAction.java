package resources;

import entities.Entity;
import entities.Player;

public class OnHitAction{
	Entity hitter;
	Player wasHit;
	public OnHitAction(){
		
	}
	public void setHitter(Entity e){
		hitter = e;
	}
	public void setHit(Player p){
		wasHit = p;
	}
	public Entity getHitter(){
		return hitter;
	}
	public Player getHit(){
		return wasHit;
	}
	public void f(){
		
	}
	public void trip(){
		f();
	}
}
