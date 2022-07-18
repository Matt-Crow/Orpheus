package orpheus.client.gui.pages.customize;

import world.build.BuildJsonUtil;
import orpheus.client.gui.components.FileChooserUtil;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.JOptionPane;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.menu.StartPlay;
import orpheus.client.gui.pages.PageController;

/**
 *
 * @author Matt
 */
public class CustomizeMain extends Page{
    public CustomizeMain(PageController host, ComponentFactory cf){
        super(host, cf);
        
        addBackButton(new StartPlay(host, cf));
        
        addMenuItem(cf.makeButton("Import Builds", ()->{
            FileChooserUtil.chooseJsonFile("Choose the build file", (f)->{
                BuildJsonUtil.loadFile(f);
            });
        }));
        
        addMenuItem(cf.makeButton("Export Builds", ()->{
            FileChooserUtil.chooseDir("Choose a direcory to save builds to", (f)->{
                String name = JOptionPane.showInputDialog("Enter a name for this export:");
                File buildFile = new File(f.getAbsolutePath() + "/" + name);
                BuildJsonUtil.saveAllToFile(buildFile);
            });
        }));
        
        setLayout(new GridLayout(1, 4));
        
        add(cf.makeButton("Customize Builds", ()->{
            getHost().switchToPage(new CustomizeChooseBuild(host, cf));
        }));
    }
}
