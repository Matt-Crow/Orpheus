package orpheus.client;

import orpheus.client.gui.pages.WorldCanvasTester;
import start.PageController;

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
            new PageController(); // todo replace with app
        }
    }
}
