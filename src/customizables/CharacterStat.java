package customizables;

import upgradables.AbstractStat;

public class CharacterStat extends AbstractStat<CharacterStatName>{
    
    public CharacterStat(CharacterStatName n, double base, double maxRelativeToMin) {
        super(n, base, maxRelativeToMin);
    }
    public CharacterStat(CharacterStatName n, double val){
        super(n, val);
    }
}
