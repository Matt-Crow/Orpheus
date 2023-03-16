package orpheus.core;

import util.Settings;
import world.builds.DataSet;

/**
 * the top-level, psuedo-static application context
 */
public class AppContext {
    /**
     * various settings and constants used by the app
     */
    private final Settings settings;

    /**
     * the builds, character classes, actives, and passives available to the app
     */
    private final DataSet dataSet;

    /**
     * Ideally, only one instance of this class should be constructed, though it
     * it not required.
     * 
     * @param settings the settings this app should use and modify
     */
    public AppContext(Settings settings) {
        this.settings = settings;
        dataSet = new DataSet();
        dataSet.loadDefaults();
    }

    /**
     * @return the settings associated with this app
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @return the set of character build data available to the app
     */
    public DataSet getDataSet() {
        return dataSet;
    }
}
