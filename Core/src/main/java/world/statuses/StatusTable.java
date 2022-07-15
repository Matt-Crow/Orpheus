package world.statuses;

import java.io.Serializable;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// will probably phase out of future versions
public final class StatusTable implements Serializable{
	/**
	 * Used to store status data for actives and passives
	 */
    
	private final HashMap<StatusName, AbstractStatus> statuses;
    
	public StatusTable(){
        statuses = new HashMap<>();
	}
    
	public final StatusTable copy(){
		StatusTable ret = new StatusTable();
        statuses.values().forEach((AbstractStatus s) -> ret.add(s));
		return ret;
	}
    
    public final void clear(){
        statuses.clear();
    }
    
	public final void add(AbstractStatus s){
		statuses.put(s.getStatusName(), s.copy());
	}
    
    public final void forEach(Consumer<AbstractStatus> f){
        statuses.values().forEach((s) -> {
            f.accept(s.copy());
        });
    }
    
    public final Stream<AbstractStatus> stream(){
        return statuses.values().stream();
    }
    
    public final AbstractStatus[] getStatuses(){
        return statuses.values().toArray(new AbstractStatus[statuses.size()]);
    }
	
	public final String getStatusString(){
		String desc = "~~~STATUS DATA~~~\n";
        desc += statuses
            .values()
            .stream()
            .map((status)->status.getDesc())
            .collect(Collectors.joining("", "* ", "\n"));
		
        desc += "~~~~~~~~~~~~~~~~~\n";
		return desc;
	}
}
