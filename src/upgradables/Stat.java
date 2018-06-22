package upgradables;
import resources.Op;

// T is an enum
public class Stat<T> {
	private T name;
	private double baseValue;
	private double maxValue;
	private double step;
	private int level;
	private double value;
	
	public Stat(T n, double base, double maxRelativeToMin){
		name = n;
		baseValue = base;
		maxValue = baseValue * maxRelativeToMin;
		step = (maxValue - base) / 10;
		level = 0;
	}
	public Stat(T n, double val){
		this(n, val, 1.0);
		name = n;
		baseValue = val;
		maxValue = val;
		step = 0;
		level = 0;
	}
	public T getName(){
		return name;
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
		Op.add(name.toString());
		Op.add(baseValue + "-" + maxValue);
		Op.add(step + " step");
		Op.add("at level " + level + ":");
		Op.add(value + " ");
		Op.dp();
	}
	public double get(){
		calc();
		return value;
	}
}
