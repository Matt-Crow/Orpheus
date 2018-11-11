package upgradables;
import resources.Op;

// T is an enum
public abstract class AbstractStat<T> {
    public final T name;
    private final double baseValue;
    private final double maxValue;
    private final double step;
    private int level;
    private double value;

    public AbstractStat(T n, double base, double maxRelativeToMin){
        name = n;
        baseValue = base;
        maxValue = baseValue * maxRelativeToMin;
        step = (maxValue - base) / 10;
        level = 0;
        calc();
    }
    public AbstractStat(T n, double val){
        this(n, val, 1.0);
    }
    public final void upgrade(){
        level += 1;
        calc();
    }
    public final void setLevel(int lv){
        level = lv;
        if(level > 10){
            level = 10;
        }
        calc();
    }
    public final void calc(){
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
    public final double get(){
        return value;
    }
}
