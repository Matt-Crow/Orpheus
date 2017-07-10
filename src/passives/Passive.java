package passives;

import java.util.ArrayList;
import entities.Player;

public class Passive {
	private static ArrayList<Passive> passiveList = new ArrayList<>();
	private String name;
	private String type;
	private Player registeredPlayer;
	
	public Passive(String n, String t){
		name = n;
		type = t;
		passiveList.add(this);
	}
	
	public static Passive getPassiveByName(String n){
		for(Passive p : passiveList){
			if(p.getName() == n){
				return p;
			}
		}
		return new Passive("PASSIVE NOT FOUND", "ERROR");
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	public Player getPlayer(){
		return registeredPlayer;
	}
	public void registerTo(Player p){
		registeredPlayer = p;
	}
	public void applyEffect(){
		
	}
	public void update(){
		
	}
}
