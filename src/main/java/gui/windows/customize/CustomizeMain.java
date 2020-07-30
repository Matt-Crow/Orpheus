package gui.windows.customize;

import util.Settings;
import customizables.BuildJsonUtil;
import gui.FileChooserUtil;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import gui.windows.Page;
import gui.windows.start.StartPlay;

/**
 *
 * @author Matt
 */
public class CustomizeMain extends Page{
    public CustomizeMain(){
        super();
        
        addBackButton(new StartPlay());
        JButton imp = new JButton("Import all customizables from a JAR file");
        imp.addActionListener((ActionEvent e)->{
            /*
            File[] chosen = FileChooserUtil.chooseFiles();
            if(chosen != null){
                for(File f : chosen){
                    if(f.getName().endsWith(".jar")){
                        Settings.getDataSet().loadFile(f);
                    }
                }
            }*/
        });
        addMenuItem(imp);
        
        JButton export = new JButton("Export all customizables to a file");
        export.addActionListener((ActionEvent e)->{
            FileChooserUtil.chooseDir("Choose a directory to create the export in", (f)->{
                String exportName = JOptionPane.showInputDialog("Enter a name for this export:");
                File dir = new File(f.getAbsolutePath() + "/" + exportName);
                dir.mkdir();
                throw new UnsupportedOperationException("Need to save customizables to a JAR file here");
            });
        });
        addMenuItem(export);
        
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
            getHost().switchToPage(new CustomizeChooseBuild());
        });
        add(bui);
    }
}
