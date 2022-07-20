package orpheus.client.gui.pages;

import util.Settings;
import world.build.Build;
import world.build.DataSet;
import world.build.actives.AbstractActive;
import world.build.characterClass.CharacterClass;
import world.build.passives.AbstractPassive;
import orpheus.client.gui.components.CustomizableSelector;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.menu.StartPlay;

/**
 *
 * @author Matt
 */
public class CustomizeBuild extends Page{
    private final JTextField name;
    private final CustomizableSelector charClassSel;
    private final CustomizableSelector[] acts;
    private final CustomizableSelector[] pass;
    
    public CustomizeBuild(PageController host, ComponentFactory cf){
        super(host, cf);
        GridLayout g = new GridLayout(3, 3);
        g.setHgap(10);
        g.setVgap(10);
        setLayout(g);
        JPanel nameArea = new JPanel();
        name = new JTextField();
        name.setToolTipText("Build name");
        //name.setEditable(true);
        nameArea.add(name);
        add(nameArea);
        
        charClassSel = new CustomizableSelector(cf, "Character Class", new CharacterClass[]{});
        add(charClassSel);
        
        add(cf.makeButton("Save and exit", ()->{
            save();
            getHost().switchToPage(new StartPlay(host, cf));
        }));
        
        acts = new CustomizableSelector[3];
        for(int i = 0; i < 3; i++){
            acts[i] = new CustomizableSelector(cf, "Active #" + (i + 1), new AbstractActive[]{});
            add(acts[i]);
        }
        
        pass = new CustomizableSelector[3];
        for(int i = 0; i < 3; i++){
            pass[i] = new CustomizableSelector(cf, "Passive #" + (i + 1), new AbstractPassive[]{});
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
