package customizables;
import upgradables.Stat;

public class CharacterClass {
	private Stat baseHP;
	private Stat baseRegenPerc;
	private Stat baseRegenWait;
	
	public void setHPData(double HP, double regen, double wait){
		baseHP = new Stat("HP", 500 * HP, 2);
		baseRegenPerc = new Stat("Healing", 5 * regen);
		baseRegenWait = new Stat("Heal rate", 20 * wait);
	}
	public void calcStats(){
		
	}
	
	public void displayHPData(){
		
	}
}
