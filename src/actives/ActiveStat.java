package actives;

import upgradables.AbstractStat;

public class ActiveStat extends AbstractStat<ActiveStatName>{
    
    public ActiveStat(ActiveStatName n, double base, double maxRelativeToMin) {
        super(n, base, maxRelativeToMin);
    }
    public ActiveStat(ActiveStatName n, double val){
        super(n, val);
    }
}
