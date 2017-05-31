package customizables;

public class Warrior extends CharacterClass{
	public Warrior(){
		super();
		super.setHPData(1.05, 0.5, 0.5);
		super.setEnergyData(0.7, 0.5, 0.5, 1, 1);
		super.setCombatData(1.2, 1.2);
	}
}
