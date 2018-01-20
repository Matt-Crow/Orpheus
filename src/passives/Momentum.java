package passives;

import statuses.Rush;

public class Momentum extends OnMeleeHitPassive{
	public Momentum(){
		super("Momentum", 20);
	}
	public void applyEffect(){
		getRegisteredTo().inflict(new Rush(1, 20));
	}
}
