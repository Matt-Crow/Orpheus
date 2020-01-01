package windows.customize;

import customizables.Build;
import customizables.BuildJsonUtil;
import customizables.CustomizableJsonUtil;
import customizables.CustomizableType;
import customizables.actives.ActiveJsonUtil;
import customizables.characterClass.CharacterClassJsonUtil;
import customizables.passives.PassiveJsonUtil;
import gui.FileChooserUtil;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import windows.Page;
import windows.start.StartPlay;

/**
 *
 * @author Matt
 */
public class CustomizeMain extends Page{
    public CustomizeMain(){
        super();
        
        addBackButton(new StartPlay());
        JButton imp = new JButton("Import all customizables from a file");
        imp.addActionListener((ActionEvent e)->{
            File[] chosen = FileChooserUtil.chooseFiles();
            if(chosen != null){
                for(File f : chosen){
                    CustomizableJsonUtil.loadFile(f);
                }
            }
        });
        addMenuItem(imp);
        
        JButton export = new JButton("Export all customizables to a file");
        export.addActionListener((ActionEvent e)->{
            File f = FileChooserUtil.chooseDir();
            if(f != null){
                String exportName = JOptionPane.showInputDialog("Enter a name for this export:");
                File dir = new File(f.getAbsolutePath() + "/" + exportName);
                dir.mkdir();
                
                ActiveJsonUtil.saveAllToFile(new File(dir.getAbsolutePath() + "/actives.json"));
                PassiveJsonUtil.saveAllToFile(new File(dir.getAbsolutePath() + "/passives.json"));
                CharacterClassJsonUtil.saveAllToFile(new File(dir.getAbsolutePath() + "/characterClasses.json"));
            }
        });
        addMenuItem(export);
        
        JButton impBuild = new JButton("Import Builds");
        impBuild.addActionListener((e)->{
            File[] chosen = FileChooserUtil.chooseFiles();
            if(chosen != null){
                for(File f : chosen){
                    BuildJsonUtil.loadFile(f);
                }
            }
        });
        addMenuItem(impBuild);
        
        JButton expBuild = new JButton("Export Builds");
        expBuild.addActionListener((e)->{
            File dir = FileChooserUtil.chooseDir();
            if(dir != null){
                String name = JOptionPane.showInputDialog("Enter a name for this export:");
                File buildFile = new File(dir.getAbsolutePath() + "/" + name);
                BuildJsonUtil.saveAllToFile(buildFile);
            }
        });
        addMenuItem(expBuild);
        
        
        
        setLayout(new GridLayout(1, 4));
        
        JButton act = new JButton("Customize Actives");
        act.addActionListener((e)->{
            getHost().switchToPage(new CustomizeChoose(CustomizableType.ACTIVE));
        });
        add(act);
        
        JButton pas = new JButton("Customize Passives");
        pas.addActionListener((e)->{
            getHost().switchToPage(new CustomizeChoose(CustomizableType.PASSIVE));
        });
        add(pas);
        
        JButton cha = new JButton("Customize Character Classes");
        cha.addActionListener((e)->{
            getHost().switchToPage(new CustomizeChoose(CustomizableType.CHARACTER_CLASS));
        });
        add(cha);
        
        JButton bui = new JButton("Customize Builds");
        bui.addActionListener((e)->{
            getHost().switchToPage(new CustomizeChooseBuild());
        });
        add(bui);
    }
}
