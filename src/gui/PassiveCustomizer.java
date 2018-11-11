package gui;

import passives.AbstractPassive;
import passives.PassiveStatName;

@SuppressWarnings("serial")
public class PassiveCustomizer extends UpgradableCustomizer<PassiveStatName>{
	public PassiveCustomizer(AbstractPassive a){
		super(a);
		switch(a.getType()){
		case THRESHOLD:
			addBox(PassiveStatName.THRESHOLD);
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
		p.setStat(PassiveStatName.THRESHOLD, val);
		p.init();
		super.updateField(n, val);
	}
	public void save(){
		super.save();
		AbstractPassive.addPassive((AbstractPassive)getCustomizing());
	}
}
