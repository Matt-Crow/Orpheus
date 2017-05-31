package upgradables;

public class Boost {
	private double amount;
	public int duration;
	
	public Boost(double boost, int dur){
		amount = boost;
		duration = dur;
	}
	public double get(){
		return amount;
	}
	public void deplete(){
		duration -= 1;
	}
}
