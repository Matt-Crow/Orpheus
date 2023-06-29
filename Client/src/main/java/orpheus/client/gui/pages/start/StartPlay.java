package orpheus.client.gui.pages.start;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Arrays;

import javax.json.JsonObject;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.components.FileChooserUtil;
import orpheus.client.gui.components.HubForm;
import orpheus.client.gui.components.SpecificationSelector;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.worldselect.WSNewMulti;
import orpheus.client.gui.pages.worldselect.WSSolo;
import serialization.JsonUtil;
import world.builds.Build;
import world.builds.BuildJsonDeserializer;
import world.builds.DataSet;
import orpheus.client.gui.pages.PageController;
import orpheus.client.gui.pages.CustomizeBuildPage;

/**
 *
 * @author Matt
 */
public class StartPlay extends Page{
    public StartPlay(ClientAppContext context, PageController host){
        super(context, host);

        var cf = context.getComponentFactory();
        
        addBackButton("Main Menu", ()-> new StartPage(context, host));
        addMenuItem(cf.makeButton("Import Builds", this::showImportBuildsDialog));
        addMenuItem(cf.makeButton("Export Builds", this::showExportBuildsDialog));
        
        setLayout(new BorderLayout());
        
        var playPanel = cf.makePanel();
        playPanel.setLayout(new BoxLayout(playPanel, BoxLayout.Y_AXIS));
        playPanel.add(cf.makeLabel("Play a game"));
        playPanel.add(cf.makeButton("Solo", ()->{
            getHost().switchToPage(new WSSolo(context, host));
        }));
        playPanel.add(cf.makeButton("Host a multiplayer game", ()->{
            getHost().switchToPage(new WSNewMulti(context, host));
        }));
        playPanel.add(new HubForm(context, "Connect to a Hub", host));
        add(cf.makeSpaceAround(playPanel), BorderLayout.LINE_START);
        
        JPanel buildSection = cf.makePanel();
        buildSection.setLayout(new BorderLayout());
        buildSection.add(cf.makeLabel("Your Builds"), BorderLayout.PAGE_START);
        var bs = new SpecificationSelector<Build>(cf, Arrays.asList(context.getDataSet().getAllBuilds()));
        buildSection.add(bs, BorderLayout.CENTER);
        buildSection.add(cf.makeButton("Customize this build", () -> {
            var selected = bs.getSelected();
            if (selected.isPresent()) {
                host.switchToPage(new CustomizeBuildPage(
                    context,
                    host,
                    selected.get()
                ));
            }
        }), BorderLayout.PAGE_END);
        
        add(buildSection, BorderLayout.LINE_END);
    }
    
    
    private void showImportBuildsDialog(){
        FileChooserUtil.chooseJsonFile("Choose the build file", (f)->{
            loadFileInto(f, getContext().getDataSet());
        });
    }

    /**
     * adds all builds from the given file into the given data set, overriding
     * any existing ones with the same names, without deleting any existing ones
     * @param f the file to read
     * @param dataSet the data set to load new builds into
     */
    private void loadFileInto(File f, DataSet dataSet){
        var buildDeserializer = new BuildJsonDeserializer();
        for(JsonObject obj : JsonUtil.readFromFile(f)){
            dataSet.addBuild(buildDeserializer.fromJson(obj));
        }
    }
    
    private void showExportBuildsDialog(){
        FileChooserUtil.chooseDir("Choose a direcory to save builds to", (f)->{
            String name = JOptionPane.showInputDialog("Enter a name for this export:");
            File buildFile = new File(f.getAbsolutePath() + "/" + name);
            saveAllToFile(getContext().getDataSet(), buildFile);
        });
    }

    private void saveAllToFile(DataSet dataSet, File f){
        JsonObject[] objs = Arrays.stream(dataSet.getAllBuilds())
            .map(Build::toJson)
            .toArray(JsonObject[]::new);
        JsonUtil.writeToFile(objs, f);  
    }
}
