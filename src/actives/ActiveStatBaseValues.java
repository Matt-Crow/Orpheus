package actives;

public class ActiveStatBaseValues {
	/**
	 * The 1-5 stats used by AbstractActive
	 */
	private int cost;
	private int cooldown;
	private int range;
	private int speed;
	private int aoe;
	private int dmg;
	
	public ActiveStatBaseValues(int c, int cd, int r, int s, int a, int d){
		cost = c;
		cooldown = cd;
		range = r;
		speed = s;
		aoe = a;
		dmg = d;
	}
	public int getCost(){
		return cost;
	}
	public int getCooldown(){
		return cooldown;
	}
	public int getRange(){
		return range;
	}
	public int getSpeed(){
		return speed;
	}
	public int getAoe(){
		return aoe;
	}
	public int getDmg(){
		return dmg;
	}
}
