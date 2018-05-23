package statuses;

import entities.Player;

public class Status {
	private StatusName code;
	private String name;
	private int level;
	private int uses;
	private int usesLeft;
	private boolean shouldTerminate;
	
	public Status(StatusName enumName, String n, int lv, int use){
		code = enumName;
		name = n;
		level = lv;
		uses = use;
		usesLeft = use;
		shouldTerminate = false;
	}
	
	public static Status decode(StatusName n, int intensity, int dur){
		Status ret = new Strength(1, 1);
		switch(n){
		case BURN:
			ret = new Burn(intensity, dur);
			break;
		case CHARGE:
			ret = new Charge(intensity, dur);
			break;
		case HEALING:
			ret = new Healing(intensity);
			break;
		case REGENERATION:
			ret = new Regeneration(intensity, dur);
			break;
		case RESISTANCE:
			ret = new Resistance(intensity, dur);
			break;
		case RUSH:
			ret = new Rush(intensity, dur);
			break;
		case STRENGTH:
			ret = new Strength(intensity, dur);
			break;
		case STUN:
			ret = new Stun(intensity, dur);
			break;
		default:
			throw new NullPointerException("Status not found: " + n);	
		}
		return ret;
	}
	
	public StatusName getStatusName(){
		return code;
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
		if(usesLeft <= 0){
			terminate();
		}
	}
}
