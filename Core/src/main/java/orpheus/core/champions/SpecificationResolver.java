package orpheus.core.champions;

import world.builds.AssembledBuild;
import world.builds.Build;
import world.builds.DataSet;

/**
 * Resolves Builds into AssembledBuilds,
 * and ChampionSpecifications into Champions.
 */
public class SpecificationResolver {

    private final DataSet dataSet;

    public SpecificationResolver(DataSet dataSet) {
        this.dataSet = dataSet;
    }
    
    public AssembledBuild resolve(Build build) {
        return dataSet.assemble(build);
    }

    public Champion resolve(ChampionSpecification specification) {
        return dataSet.getChampionByName(specification.getName());
    }
    
    public Playable resolve(Specification specification) {
        // for some reason, Java does not dispatch to the overloads
        if (specification instanceof Build) {
            return resolve((Build)specification);
        }
        if (specification instanceof ChampionSpecification) {
            return resolve((ChampionSpecification)specification);
        }
        throw new UnsupportedOperationException();
    }
}
