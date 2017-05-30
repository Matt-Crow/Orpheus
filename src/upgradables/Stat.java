package upgradables;
import resources.Op;

//add boosts
public class Stat {
	public String name;
	private double baseValue;
	private double maxValue;
	private double step;
	private int level;
	private double value;
	
	public Stat(String n, double base, double maxRelativeToMin){
		name = n;
		baseValue = base;
		maxValue = baseValue * maxRelativeToMin;
		step = (maxValue - base) / 10;
		level = 0;
	}
	public Stat(String n, double val){
		name = n;
		baseValue = val;
		maxValue = val;
		step = 0;
		level = 0;
	}
	public void upgrade(){
		level += 1;
	}
	public void setLevel(int lv){
		level = lv;
		if(level > 10){
			level = 10;
		}
	}
	public void calc(){
		 value = baseValue + step * level;
	}
	public void displayData(){
		Op.add(name);
		Op.add(baseValue + "-" + maxValue);
		Op.add(step + " step");
		Op.add("at level " + level + ":");
		Op.add(value + " ");
		Op.dp();
	}
	public double get(){
		return value;
	}
}
