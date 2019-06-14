package statuses;

import java.io.Serializable;
import java.util.ArrayList;

// will probably phase out of future versions
public class StatusTable implements Serializable{
	/**
	 * Used to store status data for actives and passives
	 */
    
	private final ArrayList<AbstractStatus> statuses;
    
	public StatusTable(){
        statuses = new ArrayList<>();
	}
	public StatusTable copy(){
		StatusTable ret = new StatusTable();
        statuses.forEach((AbstractStatus s) -> ret.add(s));
		return ret;
	}
	public void add(AbstractStatus s){
		statuses.add(s.copy());
	}
    
    /**
     * 
     * @param i
     * @return a copy of the status at index i
     */
	public AbstractStatus getStatusAt(int i){
		return statuses.get(i).copy();
	}
	public int getSize(){
		return statuses.size();
	}
	
	public String getStatusString(){
		String desc = "~~~STATUS DATA~~~";
		for(int i = 0; i < getSize(); i++){
			desc += "\n" + statuses.get(i).getDesc();
		}
		return desc;
	}
}
