package windows.customize;

import customizables.Build;
import customizables.actives.AbstractActive;
import customizables.characterClass.CharacterClass;
import customizables.passives.AbstractPassive;
import gui.CustomizableSelector;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import windows.NewPage;

/**
 *
 * @author Matt
 */
public class CustomizeBuild extends NewPage{
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
            getHost().switchToPage(new CustomizePage(getHost()));
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
        
        charClassSel.setOptions(CharacterClass.getAll());
        for(int i = 0; i < 3; i++){
            acts[i].setOptions(AbstractActive.getAll());
            pass[i].setOptions(AbstractPassive.getAll());
        }
    }

    public void setCustomizing(Build selectedBuild) {
        name.setText(selectedBuild.getName());
        charClassSel.setSelected(CharacterClass.getCharacterClassByName(selectedBuild.getClassName()));
        String[] actNames = selectedBuild.getActiveNames();
        String[] pasNames = selectedBuild.getPassiveNames();
        for(int i = 0; i < 3; i++){
            acts[i].setSelected(AbstractActive.getActiveByName(actNames[i]));
            pass[i].setSelected(AbstractPassive.getPassiveByName(pasNames[i]));
        }
    }
    
    private void save(){
        Build.addBuild(new Build(
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
