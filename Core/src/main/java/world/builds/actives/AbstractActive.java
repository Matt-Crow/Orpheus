package world.builds.actives;

import java.util.ArrayList;
import java.util.List;

import orpheus.core.world.graph.Graphable;
import orpheus.core.world.occupants.players.attributes.requirements.ActivationRequirement;
import orpheus.core.world.occupants.players.attributes.requirements.ActivationRequirements;
import orpheus.core.world.occupants.players.attributes.requirements.CooldownRequirement;
import util.Settings;
import world.builds.AbstractTriggerableAttribute;

/**
 * The AbstractActive class serves as the base for active abilities possessed by Players
 * @author Matt
 */
public abstract class AbstractActive extends AbstractTriggerableAttribute implements Graphable{
    
    /**
     * the requirements which must be met in order to use this active
     */
    private final ActivationRequirements activationRequirements;

    /**
     * must maintain a reference to this, as some of it's methods do not exist
     * on ActivationRequirements.
     */
    private final CooldownRequirement cooldown;
    
    /**
     * @param name the name of this active
     * @param activationRequirements the conditions which must be met in order
     *  to use this active. Note that a one-second cooldown is automatically 
     *  added.
     */
    public AbstractActive(String name, ActivationRequirement... activationRequirements){
        super(name);
        cooldown = new CooldownRequirement(Settings.seconds(1));

        var reqs = new ArrayList<ActivationRequirement>();
        reqs.add(cooldown);
        reqs.addAll(List.of(activationRequirements));
        this.activationRequirements = new ActivationRequirements(reqs);
    }
    
    /**
     * Adds a requirement to the list of requirements which must be met for this
     * to be used. This method is required for actives whose requirements must
     * access 'this' or fields belonging to a subclass instance.
     * @param requirement the requirement to add
     */
    protected void andRequires(ActivationRequirement requirement) {
        activationRequirements.add(requirement);
    }
    
    /**
     * uses this active, if it can be used right now
     */
    public final void useIfAble(){
        if(activationRequirements.areMet()){
            cooldown.setToCooldown();
            use();
        }
    }

    /**
     * @return a list of messages detailing why this cannot be used
     */
    protected List<String> getUnavailabilityMessages() {
        return activationRequirements.getUnavailabilityMessages();
    }
    
    @Override
    public void init(){
        cooldown.init();
    }

    @Override
    public void update(){
        cooldown.update();
    }

    @Override
    public orpheus.core.world.graph.Active toGraph() {
        return new orpheus.core.world.graph.Active(getName(), getUnavailabilityMessages());
    }

    @Override
    public abstract AbstractActive copy();

    /**
     * called whenever a player triggers this active when it is usable.
     */
    protected abstract void use();
}