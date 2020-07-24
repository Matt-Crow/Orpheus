package windows.customize;

import controllers.Settings;
import customizables.Build;
import customizables.DataSet;
import customizables.actives.AbstractActive;
import customizables.characterClass.CharacterClass;
import customizables.passives.AbstractPassive;
import gui.CustomizableSelector;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import windows.Page;

/**
 *
 * @author Matt
 */
public class CustomizeBuild extends Page{
    private final JTextArea name;
    private final CustomizableSelector charClassSel;
    private final CustomizableSelector[] acts;
    private final CustomizableSelector[] pass;
    
    public CustomizeBuild(){
        super();
        GridLayout g = new GridLayout(3, 3);
        g.setHgap(10);
        g.setVgap(10);
        setLayout(g);
        JPanel nameArea = new JPanel();
        name = new JTextArea("Build name");
        name.setEditable(true);
        nameArea.add(name);
        add(nameArea);
        
        charClassSel = new CustomizableSelector("Character Class", new CharacterClass[]{});
        add(charClassSel);
        
        JButton save = new JButton("Save and exit");
        save.addActionListener((e)->{
            save();
            getHost().switchToPage(new CustomizeMain());
        });
        add(save);
        
        acts = new CustomizableSelector[3];
        for(int i = 0; i < 3; i++){
            acts[i] = new CustomizableSelector("Active #" + (i + 1), new AbstractActive[]{});
            add(acts[i]);
        }
        
        pass = new CustomizableSelector[3];
        for(int i = 0; i < 3; i++){
            pass[i] = new CustomizableSelector("Passive #" + (i + 1), new AbstractPassive[]{});
            add(pass[i]);
        }
        
        DataSet ds = Settings.getDataSet();
        
        charClassSel.setOptions(ds.getAllCharacterClasses());
        for(int i = 0; i < 3; i++){
            acts[i].setOptions(ds.getAllActives());
            pass[i].setOptions(ds.getAllPassives());
        }
    }

    public void setCustomizing(Build selectedBuild) {
        DataSet ds = Settings.getDataSet();
        name.setText(selectedBuild.getName());
        charClassSel.setSelected(ds.getCharacterClassByName(selectedBuild.getClassName()));
        String[] actNames = selectedBuild.getActiveNames();
        String[] pasNames = selectedBuild.getPassiveNames();
        for(int i = 0; i < 3; i++){
            acts[i].setSelected(ds.getActiveByName(actNames[i]));
            pass[i].setSelected(ds.getPassiveByName(pasNames[i]));
        }
    }
    
    private void save(){
        Settings.getDataSet().addBuild(new Build(
            name.getText(),
            charClassSel.getSelected().getName(),
            acts[0].getSelected().getName(),
            acts[1].getSelected().getName(),
            acts[2].getSelected().getName(),
            pass[0].getSelected().getName(),
            pass[1].getSelected().getName(),
            pass[2].getSelected().getName()
        ));
    }
}
