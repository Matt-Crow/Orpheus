package gui;

import actives.AbstractActive;
import actives.ActiveStat;

@SuppressWarnings("serial")
public class ActiveCustomizer extends UpgradableCustomizer{
	public ActiveCustomizer(AbstractActive a){
		super(a);
		switch(a.getType()){
		case MELEE:
			addBox(ActiveStat.COOLDOWN.toString());
			addBox(ActiveStat.DAMAGE.toString());
			break;
		case BOOST:
			addBox(ActiveStat.COST.toString());
			addBox(ActiveStat.COOLDOWN.toString());
			break;
		case ELEMENTAL:
			addBox(ActiveStat.COST.toString());
			addBox(ActiveStat.COOLDOWN.toString());
			addBox(ActiveStat.RANGE.toString());
			addBox(ActiveStat.SPEED.toString());
			addBox(ActiveStat.AOE.toString());
			addBox(ActiveStat.DAMAGE.toString());
			break;
		}
	}
}
