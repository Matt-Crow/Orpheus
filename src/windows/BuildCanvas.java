package windows;

import customizables.passives.AbstractPassive;
import customizables.passives.PassiveStatName;
import customizables.actives.ActiveStatName;
import customizables.actives.AbstractActive;
import customizables.Build;
import customizables.characterClass.CharacterStatName;
import customizables.characterClass.CharacterClass;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.ArrayList;
import entities.Player;
import gui.*;
import java.io.File;

//need to redo this
@SuppressWarnings("serial")
public class BuildCanvas extends OldContentPage{
	private OptionBox<String> baseBuild;
	private CustomizableSelector classSelect;
	private CustomizableSelector[] actives;
	private CustomizableSelector[] passives;
	
	private JButton temp;
	
	private JButton setClass;
	private JButton finish;
	private JTextArea name;
	private Player testPlayer;
	
	public void phase2(){
		remove(temp);
		testPlayer = new Player("TEST");
		Build b = Build.getBuildByName(baseBuild.getSelected().toString());
		testPlayer.applyBuild(b);
		
		add(new JComponent(){});
		name = new JTextArea(b.getName());
		add(name);
		name.setEditable(true);
        
		classSelect = new CustomizableSelector("Character Class", CharacterClass.getAll());
		classSelect.getBox().setSelectedName(b.getClassName());
		add(classSelect);
		
		actives = new CustomizableSelector[3];
		for(int i = 0; i < 3; i++){
			AbstractActive[] options = AbstractActive.getAll();
			actives[i] = new CustomizableSelector("Active #" + (i + 1), options);
			actives[i].getBox().setSelectedName(b.getActiveNames()[i]);
			add(actives[i]);
		}
		
		passives = new CustomizableSelector[3];
		for(int i = 0; i < 3; i++){
			AbstractPassive[] options = AbstractPassive.getAll();
			passives[i] = new CustomizableSelector("Passive #" + (i + 1), options);
			passives[i].getBox().setSelectedName(b.getPassiveNames()[i]);
			add(passives[i]);
		}
		
		finish = new JButton("Save");
		finish.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				Build.addBuild(new Build(
						name.getText(), 
						classSelect.getBox().getSelected().getName(), 
						actives[0].getBox().getSelected().getName(), 
						actives[1].getBox().getSelected().getName(), 
						actives[2].getBox().getSelected().getName(), 
						passives[0].getBox().getSelected().getName(), 
						passives[1].getBox().getSelected().getName(), 
						passives[2].getBox().getSelected().getName()
								));
				//switchTo(new MainCanvas());
			}
		});
		addMenuItem(finish);
		
		
		//upgradableSelectors[i].getBox().setSelected(names[i % 3]);
			
		
		
		
		//TODO: add tooltip of selected item desc instead of CustomizableSelector
		
		resizeComponents(3, 3);
		resizeMenu(2);
		revalidate();
		repaint();
	}
}
