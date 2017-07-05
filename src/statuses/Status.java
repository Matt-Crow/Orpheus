package statuses;

import entities.Player;

public class Status {
	private String name;
	private int level;
	private int duration;
	private int uses;
	private int timeLeft;
	private int usesLeft;
	private boolean shouldTerminate;
	
	public Status(String n, int lv, int dur, int use){
		name = n;
		level = lv;
		duration = dur;
		uses = use;
		shouldTerminate = false;
	}
	public String getName(){
		return name;
	}
	public int getIntensityLevel(){
		return level;
	}
	public int getDuration(){
		return duration;
	}
	public int getUses(){
		return uses;
	}
	public int getTimeLeft(){
		return timeLeft;
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
		timeLeft = duration;
		usesLeft = uses;
	}
	public void use(){
		usesLeft -= 1;
		if(usesLeft == 0){
			terminate();
		}
	}
	public void update(){
		timeLeft -= 1;
		if(timeLeft == 0){
			terminate();
		}
	}
}
