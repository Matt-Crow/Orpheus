package orpheus.client.gui.components;

/**
 *
 * @author Matt
 */
public enum FileType {
    CSV("Comma Separated Values", new String[]{"csv"}),
    JSON("JSON", new String[]{"json"}),
    JAR("JAR file", new String[]{"jar"}),
    DIR("Directory", new String[]{"directory", "folder"}),
    ANY("Any file", new String[]{});
    
    private final String name;
    private final String[] extensions;
    
    private FileType(String n, String[] possibleExtensions){
        name = n;
        extensions = possibleExtensions.clone();
    }
    
    public final String getName(){
        return name;
    }
    public final String[] getExtensions(){
        return extensions;
    }
}
