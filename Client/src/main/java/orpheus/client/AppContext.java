package orpheus.client;

import util.Settings;

/**
 * the top-level, psuedo-static application context
 */
public class AppContext {

    /**
     * various settings and constants used by the app
     */
    private final Settings settings;

    /**
     * Ideally, only one instance of this class should be constructed, though it
     * it not required.
     * 
     * @param settings the settings this app should use and modify
     */
    public AppContext(Settings settings) {
        this.settings = settings;
    }

    /**
     * @return the settings associated with this app
     */
    public Settings getSettings() {
        return settings;
    }
}
