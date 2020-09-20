package world.customizables.passives;

import world.statuses.Regeneration;


public class Revitalize extends world.customizables.passives.ThresholdPassive{
	private boolean activated;
	public Revitalize(){
		super("Revitalize", 1);
		activated = false;
	}
    
    @Override
    public void init(){
        activated = false;
    }
    
    @Override
	public void trigger(){
		if(!activated){
			getUser().inflict(new Regeneration(3, 5));
			activated = true;
            System.out.println("Revitalize activated");
		}
	}
}
