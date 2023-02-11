package orpheus.client.gui.pages.start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.components.BuildSelect;
import orpheus.client.gui.components.FileChooserUtil;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.worldselect.WSMain;
import orpheus.client.gui.pages.PageController;
import orpheus.client.gui.pages.CustomizeBuildPage;
import world.build.BuildJsonUtil;

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
        
        
        setLayout(new GridLayout(1, 2));
        
        add(cf.makeSpaceAround(cf.makeButton("Play a game", ()->{
            getHost().switchToPage(new WSMain(context, host));
        }), Color.RED));
        
        JPanel buildSection = cf.makePanel();
        buildSection.setLayout(new BorderLayout());
        buildSection.add(cf.makeLabel("Your Builds"), BorderLayout.PAGE_START);
        BuildSelect bs = new BuildSelect(context);
        bs.refreshOptions();
        buildSection.add(bs, BorderLayout.CENTER);
        buildSection.add(cf.makeButton("Customize this build", ()->{
            CustomizeBuildPage cb = new CustomizeBuildPage(
                    context,
                    host,
                    bs.getSelectedBuild()
            );
            getHost().switchToPage(cb);
        }), BorderLayout.PAGE_END);
        
        add(buildSection);
    }
    
    
    private void showImportBuildsDialog(){
        FileChooserUtil.chooseJsonFile("Choose the build file", (f)->{
            BuildJsonUtil.loadFileInto(f, getContext().getDataSet());
        });
    }
    
    private void showExportBuildsDialog(){
        FileChooserUtil.chooseDir("Choose a direcory to save builds to", (f)->{
            String name = JOptionPane.showInputDialog("Enter a name for this export:");
            File buildFile = new File(f.getAbsolutePath() + "/" + name);
            BuildJsonUtil.saveAllToFile(getContext().getDataSet(), buildFile);
        });
    }
}
