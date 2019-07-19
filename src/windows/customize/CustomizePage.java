package windows.customize;

import customizables.CustomizableJsonUtil;
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
    
    public CustomizePage(){
        super();
        JButton exit = new JButton("Quit");
        exit.addActionListener((e)->{
            StartPage p = new StartPage();
            switchToPage(p);
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
        
        addSubPage(MAIN, new CustomizeMain(this));
    }
}
