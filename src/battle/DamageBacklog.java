package battle;

import customizables.CharacterStat;
import entities.Player;
import initializers.Master;

/*
 * The damage backlog is used to keep track
 * of the damage a player has taken.
 */
public class DamageBacklog {
	private int maxHP;
	private int remHP;
	private int timeSinceLastHeal;
	private int dmg;
	private double filter;
	private Player registeredTo;
	
	public DamageBacklog(Player register){
		registeredTo = register;
		dmg = 0;
		filter = 1.0 / Master.seconds(5);
		maxHP = (int) register.getStatValue(CharacterStat.HP);
		remHP = maxHP;
		timeSinceLastHeal = 0;
	}
	public int getHP(){
		return remHP;
	}
	public double getHPPerc(){
		return remHP / registeredTo.getStatValue(CharacterStat.HP) * 100;
	}
	public void applyFilter(double f){
		filter *= f;
	}
	public void log(int damage){
		dmg += damage;
	}
	public void logPercentageDamage(double percent){
		log( (int) (registeredTo.getStatValue(CharacterStat.HP) * (percent / 100)));
	}
	public void deplete(){
		if(dmg <= 0){
			return;
		}
		int damage;
		if(dmg > registeredTo.getStatValue(CharacterStat.HP) * filter){
			damage = (int) (registeredTo.getStatValue(CharacterStat.HP) * filter);
		} else {
			damage = dmg;
		}
		remHP -= damage;
		dmg -= damage;
		
		if(remHP <= 0){
			registeredTo.terminate();
		}
	}
	public void heal(int amount){
		if(!((Master.DISABLEHEALING) || (remHP == maxHP))){
			remHP += amount;
			if(remHP > maxHP){
				remHP = maxHP;
			}
		}
	}
	public void healPerc(double percent){
		heal((int) (maxHP * (percent / 100)));
	}
	public void update(){
		/*
		Op.add("Before updating backlog for " + registeredTo.getName() + ":");
		Op.add("*HP remaining: " + remHP);
		Op.add("*Backlog: " + dmg);
		Op.add("*Backlog filter: " + filter);
		*/
		deplete();
		/*
		Op.add("After updating backlog: ");
		Op.add("*HP remaining: " + remHP);
		Op.add("*Backlog: " + dmg);
		Op.add("*Backlog filter: " + filter);
		
		Op.dp();
		*/
		filter = 1.0 / Master.seconds(5);
		
		timeSinceLastHeal += 1;
		if(timeSinceLastHeal >= Master.seconds(1)){
			timeSinceLastHeal = 0;
			healPerc(5);
		}
	}
}
