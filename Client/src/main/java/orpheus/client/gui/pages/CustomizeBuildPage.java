package orpheus.client.gui.pages;

import world.build.Build;
import world.build.actives.AbstractActive;
import world.build.characterClass.CharacterClass;
import world.build.passives.AbstractPassive;
import orpheus.client.ClientAppContext;
import orpheus.client.gui.components.BuildAttributeSelector;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JTextField;
import orpheus.client.gui.pages.start.StartPlay;

/**
 * the page where players can modify builds
 * @author Matt
 */
public class CustomizeBuildPage extends Page {
    private final JTextField name;
    private final BuildAttributeSelector<CharacterClass> charClassSel;
    private final ArrayList<BuildAttributeSelector<AbstractActive>> acts;
    private final ArrayList<BuildAttributeSelector<AbstractPassive>> pass;
    
    public CustomizeBuildPage(ClientAppContext context, PageController host, Build customizing){
        super(context, host);

        var cf = context.getComponentFactory();
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        var nameArea = cf.makePanel();
        nameArea.add(cf.makeLabel("Build name"));
        name = new JTextField();
        name.setColumns(10);
        name.setToolTipText("Build name");
        nameArea.add(name);
        add(nameArea);
        
        charClassSel = new BuildAttributeSelector<>(cf, "Character Class", new CharacterClass[]{});
        add(charClassSel);
        
        var activesArea = cf.makePanel();
        activesArea.setLayout(new GridLayout(1, 3));
        acts = new ArrayList<BuildAttributeSelector<AbstractActive>>();
        for(int i = 0; i < 3; i++){
            acts.add(new BuildAttributeSelector<>(cf, "Active #" + (i + 1), new AbstractActive[]{}));
            activesArea.add(acts.get(i));
        }
        add(activesArea);
        
        var passivesArea = cf.makePanel();
        passivesArea.setLayout(new GridLayout(1, 3));
        pass = new ArrayList<BuildAttributeSelector<AbstractPassive>>();
        for(int i = 0; i < 3; i++){
            pass.add(new BuildAttributeSelector<>(cf, "Passive #" + (i + 1), new AbstractPassive[]{}));
           passivesArea.add(pass.get(i));
        }
        add(passivesArea);
        
        add(cf.makeSpaceAround(cf.makeButton("Save and exit", ()->{
            save();
            getHost().switchToPage(new StartPlay(context, host));
        })));
        
        // populate fields
        var ds = context.getDataSet();
        
        charClassSel.setOptions(ds.getAllCharacterClasses());
        for(int i = 0; i < 3; i++){
            acts.get(i).setOptions(ds.getAllActives());
            pass.get(i).setOptions(ds.getAllPassives());
        }
        
        setCustomizing(customizing);
    }

    private void setCustomizing(Build selectedBuild) {
        var ds = getContext().getDataSet();
        name.setText(selectedBuild.getName());
        charClassSel.setSelected(ds.getCharacterClassByName(selectedBuild.getClassName()));
        String[] actNames = selectedBuild.getActiveNames();
        String[] pasNames = selectedBuild.getPassiveNames();
        for(int i = 0; i < 3; i++){
            acts.get(i).setSelected(ds.getActiveByName(actNames[i]));
            pass.get(i).setSelected(ds.getPassiveByName(pasNames[i]));
        }
    }
    
    private void save(){
        getContext().getDataSet().addBuild(new Build(
            name.getText(),
            charClassSel.getSelected().getName(),
            acts.get(0).getSelected().getName(),
            acts.get(1).getSelected().getName(),
            acts.get(2).getSelected().getName(),
            pass.get(0).getSelected().getName(),
            pass.get(1).getSelected().getName(),
            pass.get(2).getSelected().getName()
        ));
    }
}
