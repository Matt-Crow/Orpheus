package passives;

import statuses.Rush;

public class Escapist extends ThreshholdPassive {
	public Escapist(){
		super("Escapist", 25);
	}
	public void applyEffect(){
		getRegisteredTo().inflict(new Rush(2, 1));
	}
}
