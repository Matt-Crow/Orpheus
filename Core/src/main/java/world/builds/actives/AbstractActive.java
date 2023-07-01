package world.builds.actives;

import java.util.List;

import orpheus.core.world.graph.Graphable;
import world.builds.AbstractTriggerableAttribute;

/**
 * The AbstractActive class serves as the base for active abilities possessed by Players
 * @author Matt
 */
public abstract class AbstractActive extends AbstractTriggerableAttribute implements Graphable{
    /**
     * @param n the name of this active
     */
    public AbstractActive(String n){
        super(n);

        setCooldownTime(1);
    }
    
    public boolean canUse(){
        return getUser() != null && !isOnCooldown();
    }
    
    @Override
    public void init(){
        //dummy init method
    }
    
    @Override
    public final void trigger(){
        if(canUse()){
            setToCooldown();
            use();
        }
    }
    
    public abstract void use();
    
    @Override
    public void update(){
        super.update();
    }

    protected List<String> getUnavailabilityMessages() {
        return (isOnCooldown())
            ? List.of(String.format("On cooldown %3d", getFramesUntilUse()))
            : List.of();
    }
    
    @Override
    public abstract AbstractActive copy();

    @Override
    public orpheus.core.world.graph.Active toGraph() {
        return new orpheus.core.world.graph.Active(getName(), getUnavailabilityMessages());
    }
}