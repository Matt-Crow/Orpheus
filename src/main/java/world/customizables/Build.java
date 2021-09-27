package world.customizables;

import java.util.Arrays;
import java.util.stream.Collectors;
import util.Settings;


/**
 * A Build is a collection of 
 * CharacterClass, 
 * 3 active abilities,
 * and 3 passive abilities
 * which are applied to Players at the beginning of a match
 * @author Matt
 */
public class Build{	
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
    
    public String getDescription(){
        DataSet ds = Settings.getDataSet();
        
        StringBuilder sb = new StringBuilder();
        String sep = "~~~~~~~~~~~~~~~~~~~~";
        sb.append("Build ").append(name).append(": \n");
        sb.append("Class: \n").append(
            entab(
                ds.getCharacterClassByName(className).getDescription())
        )
            .append("\n");
        sb.append("Actives: \n");
        for(String an : activeNames){
            sb.append(
                entab(
                    sep + '\n' +
                    ds.getActiveByName(an).getDescription()
                )
            ).append("\n");
        }
        sb.append("Passives: \n");
        for(String pn : passiveNames){
            sb.append(
                entab(
                    sep + '\n' +
                    ds.getPassiveByName(pn).getDescription()
                )
            ).append("\n");
        }
        return sb.toString();
    }
    
    public static String entab(String s){
        return Arrays.stream(s.split("\n")).collect(Collectors.joining("\n\t", "\t", ""));
    }
}
