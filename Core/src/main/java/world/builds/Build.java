package world.builds;

import javax.json.JsonObject;

import orpheus.core.champions.BuildOrChampion;
import orpheus.core.champions.Specification;

/**
 * A Build is a collection of CharacterClass, 3 active abilities, and 3 passive 
 * abilities which are applied to Players at the beginning of a match
 * @author Matt
 */
public class Build implements Specification {	
	private final String name;
	private final String className;
	private final String[] activeNames;
	private final String[] passiveNames;
	
	public Build(String buildName, String cName, String a1, String a2, String a3, String p1, String p2, String p3){
		name = buildName;
		className = cName;
		activeNames = new String[]{a1, a2, a3};
		passiveNames = new String[]{p1, p2, p3};
	}
    
    public Build copy(){
        return new Build(
            name,
            className,
            activeNames[0],
            activeNames[1],
            activeNames[2],
            passiveNames[0],
            passiveNames[1],
            passiveNames[2]
        );
    }
    
    @Override
	public String getName(){
		return name;
	}

	public String getClassName(){
		return className;
	}
	public String[] getActiveNames(){
		return activeNames;
	}
	public String[] getPassiveNames(){
		return passiveNames;
	}

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();

        sb.append("Build\n");
        sb.append(String.format(" * name: %s\n", name));
        sb.append(String.format(" * class: %s\n", className));
        sb.append(" * actives:\n");
        for (String act : activeNames) {
            sb.append(String.format("\t * %s\n", act));
        }
        sb.append(" * passives:\n");
        for (String pas : passiveNames) {
            sb.append(String.format("\t * %s\n", pas));
        }
        return sb.toString();
    }

    @Override
    public BuildOrChampion getType() {
        return BuildOrChampion.BUILD;
    }

    @Override
    public JsonObject doToJson() {
        var asJson = BuildJsonUtil.serializeJson(this);
        return asJson;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
