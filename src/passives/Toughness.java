package passives;

import statuses.Resistance;

public class Toughness extends OnBeHitPassive{
	public Toughness(){
		super("Toughness", 20);
	}
	public void applyEffect(){
		getPlayer().inflict(new Resistance(1, 1));
	}
}
