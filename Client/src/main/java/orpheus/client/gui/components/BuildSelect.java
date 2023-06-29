package orpheus.client.gui.components;

import java.util.Arrays;

import orpheus.client.ClientAppContext;
import world.builds.AssembledBuild;
import world.builds.Build;

/**
 * A component that allows players to select which build they want to use
 * @author Matt
 */
public class BuildSelect extends SpecificationSelector<Build> {
    private final ClientAppContext ctx;
    
    public BuildSelect(ClientAppContext ctx) {
        super(
            ctx.getComponentFactory(), 
            Arrays.asList(ctx.getDataSet().getAllBuilds())
        );
        this.ctx = ctx;
    }
    
    public Build getSelectedBuild(){
        return getSelected().orElse(null);
    }

    public AssembledBuild getSelectedAssembledBuild() {
        var unassembled = getSelectedBuild();
        return (unassembled == null) 
            ? null 
            : ctx.getSpecificationResolver().resolve(unassembled);
    }
}