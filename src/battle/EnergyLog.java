package battle;

import customizables.CharacterStatName;
import entities.Player;
import controllers.Master;

public class EnergyLog {
	private int maxEnergy;
	private int energy;
	private int timeSinceLastEnergy;
	private Player registeredTo;
	
	public EnergyLog(Player register){
		registeredTo = register;
		maxEnergy = (int) register.getStatValue(CharacterStatName.ENERGY);
		energy = maxEnergy;
		timeSinceLastEnergy = 0;
	}
	public int getEnergy(){
		return energy;
	}
	public void gainEnergy(int amount){
		if(energy == maxEnergy){
			return;
		}
		energy += amount;
		if(energy > maxEnergy){
			energy = maxEnergy;
		}
	}
	public void loseEnergy(int amount){
		energy -= amount;
	}
	public void update(){
		timeSinceLastEnergy += 1;
		if(timeSinceLastEnergy >= Master.seconds(1)){
			timeSinceLastEnergy = 0;
			gainEnergy((int) registeredTo.getStatValue(CharacterStatName.ENERGY) / 20);
		}
	}
}
