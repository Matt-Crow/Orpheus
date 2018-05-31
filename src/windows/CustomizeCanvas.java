package windows;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import actives.AbstractActive;
import passives.AbstractPassive;
import resources.Op;
import upgradables.AbstractUpgradable;
import upgradables.UpgradableType;
import gui.*;

@SuppressWarnings("serial")

// looks like I'll have to do seperate active and passive customizers
public class CustomizeCanvas extends DrawingPlane{
	
	private Title title;
	private OptionBox<String> upgradableName;
	private AbstractUpgradable customizing;
	
	// used to choose the type of what to customize
	private Button act;
	private Button cha;
	private Button pas;
	
	private VoidComponent spacer;
	private Button customize;
	
	public CustomizeCanvas(){
		super();
		
		Button quit = new Button("Quit without saving");
		quit.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new BuildWindow();
				close();
			}
		});
		add(quit);
		
		title = new Title("Select what you want to customize");
		add(title);
		
		spacer = new VoidComponent();
		add(spacer);
		
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
				
			}
		});
		add(cha);
		
		pas = new Button("Passive");
		pas.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				
			}
		});
		add(pas);
		resizeComponents(2, 3);
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
		}
		/*
		if (type.equals("Class")){
			// implement later
		} else {
			AbstractPassive[] pass = AbstractPassive.getAll();
			names = new String[pass.length];
			for(int i = 0; i < pass.length; i++){
				names[i] = pass[i].getName();
			}
			customize.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					customizing = AbstractPassive.getPassiveByName(upgradableName.getSelected());
					remove(customize);
					remove(upgradableName);
					//add(new CustomizeViewer(customizing));
					revalidate();
					repaint();
				}
			});
		}*/
		upgradableName = new OptionBox<>("Select active to customize", names);
		add(upgradableName);
		resizeComponents(2, 2);
		revalidate();
		repaint();
	}
	private void phase3(UpgradableType type){
		removePhase2();
		Button save = new Button("Save changes");
		AbstractAction a = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				Op.add("Something went wrong in CustomizeCanvas.phase3...");
				Op.dp();
			}
		};
		switch(type){
		case ACTIVE:
			a = new AbstractAction(){
					public void actionPerformed(ActionEvent e){
						AbstractActive.addActive((AbstractActive)customizing);
						new MainWindow();
						close();
					}
			};
			break;
		};
		
		save.addActionListener(a);
		add(save);
		resizeComponents(2, 2);
	}
	private void removePhase1(){
		remove(act);
		remove(cha);
		remove(pas);
		remove(spacer);
		revalidate();
		repaint();
	}
	private void removePhase2(){
		remove(title);
		remove(customize);
		remove(upgradableName);
		revalidate();
		repaint();
	}
}
