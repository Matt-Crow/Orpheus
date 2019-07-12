package gui;

import customizables.passives.AbstractPassive;
import customizables.passives.PassiveStatName;
import customizables.passives.ThresholdPassive;

@SuppressWarnings("serial")
public class PassiveCustomizer extends Customizer<PassiveStatName>{
	public PassiveCustomizer(AbstractPassive a){
		super(a);
		switch(a.getPassiveType()){
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
		if(p instanceof ThresholdPassive){
            ((ThresholdPassive)p).setStat(PassiveStatName.THRESHOLD, val);
        }
        try{
            p.init();
        }catch(NullPointerException ex){
            // p's user isn't initialized yet
            System.err.println("passive init caused null pointer in PassiveCustomizer.updateField");
        }
		
		super.updateField(n, val);
	}
	public void save(){
		super.save();
		AbstractPassive.addPassive((AbstractPassive)getCustomizing());
	}
}
