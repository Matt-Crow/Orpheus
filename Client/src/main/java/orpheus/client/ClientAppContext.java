package orpheus.client;

import java.util.Optional;

import javax.swing.JOptionPane;

import orpheus.client.gui.components.ComponentFactory;
import orpheus.core.AppContext;
import orpheus.core.net.OrpheusClient;
import orpheus.core.users.LocalUser;
import util.Settings;

/**
 * the top-level, psuedo-static application context
 */
public class ClientAppContext extends AppContext {

    /**
     * the currently logged in user
     */
    private Optional<LocalUser> user = Optional.empty();

    /**
     * the client connected to a server
     */
    private Optional<OrpheusClient> client = Optional.empty();

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

    /**
     * shows the login window iff no user is logged in
     */
    public void showLoginWindow() {
        if (user.isEmpty()) {
            var name = JOptionPane.showInputDialog("Enter a username:");
            if (name != null) {
                user = Optional.of(new LocalUser(name));
            }
        }
    }

    /**
     * throws an exception if no user is logged in
     * @return
     */
    public LocalUser getLoggedInUser() {
        return user.get();
    }

    public void setClient(OrpheusClient client) {
        this.client = Optional.of(client);
    }

    public Optional<OrpheusClient> getClient() {
        return client;
    }
}
