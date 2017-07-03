package statuses;

public class Status {
	private String name;
	private int level;
	private int duration;
	private int uses;
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
	public boolean getShouldTerminate(){
		return shouldTerminate;
	}
	public void terminate(){
		shouldTerminate = true;
	}
	public void use(){
		uses -= 1;
		if(uses == 0){
			terminate();
		}
	}
	public void update(){
		duration -= 1;
		if(duration == 0){
			terminate();
		}
	}
}
