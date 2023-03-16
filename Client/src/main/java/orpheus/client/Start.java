package orpheus.client;

import orpheus.client.gui.components.ComponentFactory;
import util.Settings;
import orpheus.client.gui.pages.PageController;

/**
 * Launches the client application
 * @author Matt Crow
 */
public class Start {
    /** 
     * @param args command line arguments
     */
    public static void main(String[] args){
        var settings = new Settings();
        var context = new ClientAppContext(settings, new ComponentFactory());
        new PageController(context);
    }
}
