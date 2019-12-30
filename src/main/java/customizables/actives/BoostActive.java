package customizables.actives;

import statuses.AbstractStatus;
import statuses.StatusTable;

public class BoostActive extends AbstractActive{
    public BoostActive(String n, StatusTable t){
        super(ActiveType.BOOST, n, 0, 0, 0, 0, 0);
        setInflict(t);
    }
    public BoostActive(String n, AbstractStatus[] st){
        super(ActiveType.BOOST, n, 0, 0, 0, 0, 0);
        for(AbstractStatus s : st){
            addStatus(s);
        }
    }
    
    @Override
	public BoostActive copy(){
		BoostActive ret = new BoostActive(getName(), getInflict().copy());
        copyTagsTo(ret);
        ret.setColors(getColors());
        return ret;
	}
    @Override
	public void use(){
		super.use();
		StatusTable s = getInflict();
		for(int i = 0; i < s.getSize(); i++){
			getUser().inflict(s.getStatusAt(i));
		}
	}
    @Override
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "Upon use, inflicts the user with: \n";
		desc += getInflict().getStatusString() + "\n";
		return desc;
	}
}
