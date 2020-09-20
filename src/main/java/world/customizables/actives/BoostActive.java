package world.customizables.actives;

import world.statuses.AbstractStatus;
import world.statuses.StatusTable;

public class BoostActive extends AbstractActive{
    public BoostActive(String n, StatusTable t){
        this(n, t.getStatuses());
    }
    public BoostActive(String n, AbstractStatus[] st){
        super(n);
        addStatuses(st);
    }
    
    @Override
	public BoostActive copy(){        
        return new BoostActive(getName(), getInflict().copy());
	}
    @Override
	public void use(){
        applyEffect(getUser());
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "Upon use, inflicts the user with: \n";
		desc += getInflict().getStatusString() + "\n";
		return desc;
	}
}
