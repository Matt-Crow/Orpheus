package gui;

import actives.AbstractActive;
import actives.ActiveStat;

@SuppressWarnings("serial")
public class ActiveCustomizer extends UpgradableCustomizer{
	public ActiveCustomizer(AbstractActive a){
		super(a);
		switch(a.getType()){
		case MELEE:
			addBox(ActiveStat.DAMAGE.toString());
			break;
		case BOOST:
			// need status stuff here
			break;
		case ELEMENTAL:
			addBox(ActiveStat.ARC.toString());
			addBox(ActiveStat.RANGE.toString());
			addBox(ActiveStat.SPEED.toString());
			addBox(ActiveStat.AOE.toString());
			addBox(ActiveStat.DAMAGE.toString());
			break;
		}
	}
	public void addParticleBox(){
		// TODO: implement
	}
	public void updateField(String n, int val){
		AbstractActive a = (AbstractActive)getCustomizing();
		a.setStat(ActiveStat.valueOf(n.toUpperCase()), val);
		a.init();
		super.updateField(n, val);
	}
}
