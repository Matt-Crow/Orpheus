package windows;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import actives.AbstractActive;
import passives.AbstractPassive;
import upgradables.AbstractUpgradable;
import customizables.CharacterClass;
import upgradables.UpgradableType;
import gui.*;

@SuppressWarnings("serial")

// looks like I'll have to do seperate active and passive customizers
public class CustomizeCanvas extends DrawingPlane{
	
	private OptionBox<String> upgradableName;
	private AbstractUpgradable customizing;
	
	// used to choose the type of what to customize
	private Button act;
	private Button cha;
	private Button pas;
	
	private Button customize;
	
	public CustomizeCanvas(){
		super();
		
		Button quit = new Button("Return to build window");
		quit.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new BuildWindow();
				close();
			}
		});
		addMenuItem(quit);
		
		act = new Button("Active");
		act.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				phase2(UpgradableType.ACTIVE);
			}
		});
		add(act);
		
		cha = new Button("Character Class");
		cha.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				phase2(UpgradableType.CHARACTER_CLASS);
			}
		});
		add(cha);
		
		pas = new Button("Passive");
		pas.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				phase2(UpgradableType.PASSIVE);
			}
		});
		add(pas);
		resizeComponents(1, 3);
		resizeMenu(1);
	}
	private void phase2(UpgradableType type){
		removePhase1();
		
		String[] names = new String[]{"An error occurred in CustomizeCanvas.phase2..."};
		customize = new Button("Customize selected build");
		add(customize);
		
		switch(type){
		case ACTIVE:
			names = AbstractActive.getAllNames();
			customize.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					customizing = AbstractActive.getActiveByName(upgradableName.getSelected());
					add(new ActiveCustomizer((AbstractActive)customizing));
					phase3(UpgradableType.ACTIVE);
				}
			});
			break;
		case PASSIVE:
			names = AbstractPassive.getAllNames();
			customize.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					customizing = AbstractPassive.getPassiveByName(upgradableName.getSelected());
					add(new PassiveCustomizer((AbstractPassive)customizing));
					phase3(UpgradableType.PASSIVE);
				}
			});
			break;
		case CHARACTER_CLASS:
			names = CharacterClass.getAllNames();
			customize.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					customizing = CharacterClass.getCharacterClassByName(upgradableName.getSelected());
					add(new CharacterClassCustomizer((CharacterClass)customizing));
					phase3(UpgradableType.PASSIVE);
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
	private void phase3(UpgradableType type){
		removePhase2();
		resizeComponents(1, 2);
	}
	private void removePhase1(){
		remove(act);
		remove(cha);
		remove(pas);
		revalidate();
		repaint();
	}
	private void removePhase2(){
		remove(customize);
		remove(upgradableName);
		revalidate();
		repaint();
	}
}
