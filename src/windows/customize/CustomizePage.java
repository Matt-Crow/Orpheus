package windows.customize;

import controllers.MainWindow;
import customizables.Build;
import customizables.CustomizableJsonUtil;
import customizables.CustomizableType;
import customizables.actives.AbstractActive;
import customizables.characterClass.CharacterClass;
import customizables.passives.AbstractPassive;
import gui.FileChooserUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import windows.Page;
import windows.start.StartPage;

/**
 *
 * @author Matt
 */
public class CustomizePage extends Page{
    public static final String MAIN = "MAIN";
    public static final String CHOOSE_ACT = "CHOOSE ACT";
    public static final String CHOOSE_PAS = "CHOOSE PAS";
    public static final String CHOOSE_CHA = "CHOOSE CHA";
    public static final String CUSTOMIZE = "CUSTOMIZE";
    public static final String CHOOSE_BUI = "CHOOSE BUILD";
    public static final String CUSTOM_BUILD = "CUSTOMIZE BUILD";
    
    public CustomizePage(MainWindow host){
        super(host);
        JButton exit = new JButton("Quit");
        exit.addActionListener((e)->{
            StartPage p = new StartPage(host);
            getHost().switchToPage(p);
            p.switchToSubpage(StartPage.PLAY);
        });
        addMenuItem(exit);
        
        
        
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
                
                AbstractActive.saveAllToFile(new File(dir.getAbsolutePath() + "/actives.json"));
                AbstractPassive.saveAll(new File(dir.getAbsolutePath() + "/passives.json"));
                CharacterClass.saveAll(new File(dir.getAbsolutePath() + "/characterClasses.json"));
            }
        });
        addMenuItem(export);
        
        JButton impBuild = new JButton("Import Builds");
        impBuild.addActionListener((e)->{
            File[] chosen = FileChooserUtil.chooseFiles();
            if(chosen != null){
                for(File f : chosen){
                    Build.loadFile(f);
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
                Build.saveAllToFile(buildFile);
            }
        });
        addMenuItem(expBuild);
        
        
        
        addSubPage(MAIN, new CustomizeMain(this));
        addSubPage(CHOOSE_ACT, new CustomizeChoose(this, CustomizableType.ACTIVE));
        addSubPage(CHOOSE_PAS, new CustomizeChoose(this, CustomizableType.PASSIVE));
        addSubPage(CHOOSE_CHA, new CustomizeChoose(this, CustomizableType.CHARACTER_CLASS));
        addSubPage(CUSTOMIZE, new CustomizeCustomize(this));
        addSubPage(CHOOSE_BUI, new CustomizeChooseBuild(this));
        addSubPage(CUSTOM_BUILD, new CustomizeBuild(this));
    }
}
