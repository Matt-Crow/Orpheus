package windows;

import customizables.passives.AbstractPassive;
import customizables.actives.AbstractActive;
import customizables.CustomizableJsonUtil;
import customizables.AbstractCustomizable;
import customizables.characterClass.CharacterClass;
import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import customizables.CustomizableType;
import gui.*;

@SuppressWarnings({"serial", "rawtypes"})

// looks like I'll have to do seperate active and passive customizers
public class CustomizeCanvas extends OldContentPage{
	
	private OptionBox<String> upgradableName;
	private AbstractCustomizable customizing;
	
	
	private JButton customize;
	
	public CustomizeCanvas(){
		super();
	}
	private void phase2(CustomizableType type){
		
		String[] names = new String[]{"An error occurred in CustomizeCanvas.phase2..."};
		customize = new JButton("Customize selected build");
		add(customize);
		
		switch(type){
		case ACTIVE:
			names = AbstractActive.getAllNames();
			customize.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					customizing = AbstractActive.getActiveByName(upgradableName.getSelected());
					add(new ActiveCustomizer((AbstractActive)customizing));
					phase3(CustomizableType.ACTIVE);
				}
			});
			break;
		case PASSIVE:
			names = AbstractPassive.getAllNames();
			customize.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					customizing = AbstractPassive.getPassiveByName(upgradableName.getSelected());
					add(new PassiveCustomizer((AbstractPassive)customizing));
					phase3(CustomizableType.PASSIVE);
				}
			});
			break;
		case CHARACTER_CLASS:
			names = CharacterClass.getAllNames();
			customize.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					customizing = CharacterClass.getCharacterClassByName(upgradableName.getSelected());
					add(new CharacterClassCustomizer((CharacterClass)customizing));
					phase3(CustomizableType.PASSIVE);
				}
			});
			break;
		}
		
		upgradableName = new OptionBox<>("Select upgradable to customize", names);
		add(upgradableName);
		resizeComponents(2, 1);
		revalidate();
		repaint();
	}
	private void phase3(CustomizableType type){
		removePhase2();
		resizeComponents(1, 2);
	}
	
	private void removePhase2(){
		remove(customize);
		remove(upgradableName);
		revalidate();
		repaint();
	}
}
