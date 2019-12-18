package battle;

import customizables.characterClass.CharacterStatName;
import entities.AbstractPlayer;
import controllers.Master;
import java.io.Serializable;

public class EnergyLog implements Serializable{
	private int maxEnergy;
	private int energy;
	private int timeSinceLastEnergy;
	private AbstractPlayer registeredTo;
	
	public EnergyLog(AbstractPlayer register){
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
    public void gainEnergyPerc(double percent){
        gainEnergy((int) (maxEnergy * (percent / 100)));
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
