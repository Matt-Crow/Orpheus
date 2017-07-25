package passives;

import statuses.Charge;

public class SparkingStrikes extends OnHitPassive{
	public SparkingStrikes(){
		super("Sparking Strikes", 20);
	}
	public void applyEffect(){
		getPlayer().inflict(new Charge(1, 10));
	}
}
