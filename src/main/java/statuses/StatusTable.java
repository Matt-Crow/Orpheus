package statuses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

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
    
    public final void clear(){
        statuses.clear();
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
    
    public void forEach(Consumer<AbstractStatus> f){
        statuses.forEach((s) -> {
            f.accept(s.copy());
        });
    }
    
	public int getSize(){
		return statuses.size();
	}
	
	public String getStatusString(){
		String desc = "~~~STATUS DATA~~~\n";
		for(int i = 0; i < getSize(); i++){
			desc += statuses.get(i).getDesc() + "\n";
		}
        desc += "~~~~~~~~~~~~~~~~~\n";
		return desc;
	}
}
