package statuses;

import entities.Player;

public class Status {
	private String name;
	private int level;
	private int uses;
	private int usesLeft;
	private boolean shouldTerminate;
	
	public Status(String n, int lv, int use){
		name = n;
		level = lv;
		uses = use;
		shouldTerminate = false;
	}
	public String getName(){
		return name;
	}
	public int getIntensityLevel(){
		return level;
	}
	public int getUses(){
		return uses;
	}
	public int getUsesLeft(){
		return usesLeft;
	}
	public boolean getShouldTerminate(){
		return shouldTerminate;
	}
	public void terminate(){
		shouldTerminate = true;
	}
	public void inflictOn(Player p){
		
	}
	public void reset(){
		shouldTerminate = false;
		usesLeft = uses;
	}
	public void use(){
		usesLeft -= 1;
		if(usesLeft == 0){
			terminate();
		}
	}
}
