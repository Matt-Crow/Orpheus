package orpheus.client;

import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.WorldCanvasTester;
import util.Settings;
import orpheus.client.gui.pages.PageController;

/**
 * Launches the client application
 * @author Matt Crow
 */
public class Start {
    /**
     * Argument options:
     * * --test-canvas: run WorldCanvasTester
     * 
     * @param args command line arguments
     */
    public static void main(String[] args){
        // todo config system
        boolean testCanvas = false;
        for(String arg : args){
            if("--test-canvas".equals(arg)){
                testCanvas = true;
            }
        }
        
        if(testCanvas){
            WorldCanvasTester.main(args);
        } else {
            var settings = new Settings();
            var context = new AppContext(settings);
            new PageController(context, new ComponentFactory());
        }
    }
}
