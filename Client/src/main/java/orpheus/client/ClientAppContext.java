package orpheus.client;

import orpheus.client.gui.components.ComponentFactory;
import orpheus.core.AppContext;
import util.Settings;

/**
 * the top-level, psuedo-static application context
 */
public class ClientAppContext extends AppContext {

    /**
     * used to produce components for the GUI
     */
    private final ComponentFactory componentFactory;

    /**
     * Ideally, only one instance of this class should be constructed, though it
     * it not required.
     * 
     * @param settings the settings this app should use and modify
     */
    public ClientAppContext(Settings settings, ComponentFactory componentFactory) {
        super(settings);
        this.componentFactory = componentFactory;
    }

    /**
     * GUI components should call this method and use the result to make Swing
     * components such as buttons and text areas.
     * 
     * @return a factory which produces styled components
     */
    public ComponentFactory getComponentFactory() {
        return componentFactory;
    }
}
