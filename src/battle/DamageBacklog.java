package battle;

import entities.Player;
import resources.Op;

public class DamageBacklog {
	private int dmg;
	private double filter;
	private Player registeredTo;
	
	public DamageBacklog(Player register){
		registeredTo = register;
		dmg = 0;
		filter = 0.01;
	}
	public void applyFilter(double f){
		filter *= f;
	}
	public void log(int damage){
		dmg += damage;
	}
	public void deplete(){
		int damage;
		if(dmg > registeredTo.getStatValue("maxHP") * filter){
			damage = (int) (registeredTo.getStatValue("maxHP") * filter);
		} else {
			damage = dmg;
		}
		registeredTo.loseHP(damage);
		dmg -= damage;
	}
	public void update(){
		if(dmg <= 0){
			return;
		}
		Op.add("Before updating backlog for " + registeredTo.getName() + ":");
		Op.add("*HP remaining: " + registeredTo.getHP());
		Op.add("*Backlog: " + dmg);
		Op.add("*Backlog filter: " + filter);
		deplete();
		Op.add("After updating backlog: ");
		Op.add("*HP remaining: " + registeredTo.getHP());
		Op.add("*Backlog: " + dmg);
		Op.add("*Backlog filter: " + filter);
		
		Op.dp();
		filter = 0.01;
	}
}
