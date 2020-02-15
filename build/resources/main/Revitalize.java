//package customizables.passives;

import entities.AbstractPlayer;
import statuses.Regeneration;


// find some way to do this
public class Revitalize extends customizables.passives.ThresholdPassive{
	private boolean activated;
	public Revitalize(){
		super("Revitalize", 25);
		activated = false;
	}
    
    @Override
    public void init(){
        super.init();
        activated = false;
    }
    
    @Override
	public void applyEffect(AbstractPlayer p){
        super.applyEffect(p);
		if(!activated){
			p.inflict(new Regeneration(3, 5));
			activated = true;
            System.out.println("Revitalize activated");
		}
	}
}
