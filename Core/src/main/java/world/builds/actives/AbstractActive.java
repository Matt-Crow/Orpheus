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
    
    private final ActivationRequirements activationRequirements;
    private final CooldownRequirement cooldown;
    
    /**
     * @param n the name of this active
     */
    public AbstractActive(String n, ActivationRequirement... activationRequirements){
        super(n);
        cooldown = new CooldownRequirement(Settings.seconds(1));

        List<ActivationRequirement> reqs = new ArrayList<>();
        reqs.add(cooldown);
        reqs.addAll(List.of(activationRequirements));
        this.activationRequirements = new ActivationRequirements(reqs);
    }
    
    // required in case activation requirements need access to 'this'
    protected void andRequires(ActivationRequirement requirement) {
        activationRequirements.add(requirement);
    }

    public boolean canUse(){
        return getUser() != null && activationRequirements.areMet();
    }
    
    @Override
    public void init(){
        cooldown.init();
    }
    
    @Override
    public final void trigger(){
        if(canUse()){
            cooldown.setToCooldown();
            use();
        }
    }
    
    public abstract void use();
    
    @Override
    public void update(){
        cooldown.update();
    }

    protected List<String> getUnavailabilityMessages() {
        return activationRequirements.getUnavailabilityMessages();
    }
    
    @Override
    public abstract AbstractActive copy();

    @Override
    public orpheus.core.world.graph.Active toGraph() {
        return new orpheus.core.world.graph.Active(getName(), getUnavailabilityMessages());
    }
}