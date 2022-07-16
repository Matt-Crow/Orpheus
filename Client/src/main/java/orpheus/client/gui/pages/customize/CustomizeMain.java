package orpheus.client.gui.pages.customize;

import world.build.BuildJsonUtil;
import orpheus.client.gui.components.FileChooserUtil;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.JButton;
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
        
        JButton impBuild = new JButton("Import Builds");
        impBuild.addActionListener((e)->{
            FileChooserUtil.chooseJsonFile("Choose the build file", (f)->{
                BuildJsonUtil.loadFile(f);
            });
        });
        addMenuItem(impBuild);
        
        JButton expBuild = new JButton("Export Builds");
        expBuild.addActionListener((e)->{
            FileChooserUtil.chooseDir("Choose a direcory to save builds to", (f)->{
                String name = JOptionPane.showInputDialog("Enter a name for this export:");
                File buildFile = new File(f.getAbsolutePath() + "/" + name);
                BuildJsonUtil.saveAllToFile(buildFile);
            });
        });
        addMenuItem(expBuild);
        
        
        
        setLayout(new GridLayout(1, 4));
        
        
        JButton bui = new JButton("Customize Builds");
        bui.addActionListener((e)->{
            getHost().switchToPage(new CustomizeChooseBuild(host, cf));
        });
        add(bui);
    }
}
