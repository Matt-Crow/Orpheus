package passives;

import upgradables.AbstractStat;

public class PassiveStat extends AbstractStat<PassiveStatName>{
    
    public PassiveStat(PassiveStatName n, double base, double maxRelativeToMin) {
        super(n, base, maxRelativeToMin);
    }
    public PassiveStat(PassiveStatName n, double val){
        super(n, val);
    }
}
