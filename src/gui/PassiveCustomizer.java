package gui;

import passives.AbstractPassive;
import passives.PassiveStat;

@SuppressWarnings("serial")
public class PassiveCustomizer extends UpgradableCustomizer{
	public PassiveCustomizer(AbstractPassive a){
		super(a);
		switch(a.getType()){
		case THRESHOLD:
			addBox(PassiveStat.THRESHOLD.toString());
			addStatusBoxes();
			break;
		case ONMELEEHIT:
			addStatusBoxes();
			break;
		case ONHIT:
			addStatusBoxes();
			break;
		case ONBEHIT:
			addStatusBoxes();
		}
	}
	public void updateField(String n, int val){
		AbstractPassive p = (AbstractPassive)getCustomizing();
		p.setStat(PassiveStat.THRESHOLD, val);
		p.init();
		super.updateField(n, val);
	}
	public void save(){
		super.save();
		AbstractPassive.addPassive((AbstractPassive)getCustomizing());
	}
}
