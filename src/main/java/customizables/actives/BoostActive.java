package customizables.actives;

import statuses.AbstractStatus;
import statuses.StatusTable;

public class BoostActive extends AbstractActive{
    public BoostActive(String n, StatusTable t){
        this(n, t.getStatuses());
    }
    public BoostActive(String n, AbstractStatus[] st){
        super(ActiveType.BOOST, n);
        addStatuses(st);
    }
    
    @Override
	public BoostActive copy(){
		BoostActive ret = new BoostActive(getName(), getInflict().copy());
        copyTagsTo(ret);
        return ret;
	}
    @Override
	public void trigger(){
		super.trigger();
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
