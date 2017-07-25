package passives;

import statuses.Charge;

public class SparkingStrikes extends OnMeleeHitPassive{
	public SparkingStrikes(){
		super("Sparking Strikes", 100);
	}
	public void applyEffect(){
		getPlayer().inflict(new Charge(1, 10));
	}
}
