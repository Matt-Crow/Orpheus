package orpheus.client.gui.pages.start;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Arrays;
import java.util.List;

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
import orpheus.core.utils.OrpheusJsonHelper;
import world.builds.Build;
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
            var dataSet = getContext().getDataSet();
            try {
                BuildSaveFile
                    .fromJson(OrpheusJsonHelper.readFile(f))
                    .getBuilds()
                    .stream()
                    .forEach(dataSet::addBuild);
                JOptionPane.showMessageDialog(this, "Builds imported successfully");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred when importing builds", "Build import error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void showExportBuildsDialog(){
        FileChooserUtil.chooseDir("Choose a direcory to save builds to", (dir)->{
            String name = JOptionPane.showInputDialog("Enter a name for this export:");
            File f = new File(dir.getAbsolutePath() + "/" + name);
            try {
                var dataSet = getContext().getDataSet();
                var builds = List.of(dataSet.getAllBuilds());
                var buildSaveFile = new BuildSaveFile(builds);
                OrpheusJsonHelper.writeFile(f, buildSaveFile.toJson());
                JOptionPane.showMessageDialog(this, "Builds saved successfully");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred when saving builds", "Build save error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
